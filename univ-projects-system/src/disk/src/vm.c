/* ------------------------------
   $Id: vm-skel.c,v 1.1 2002/10/21 07:16:29 marquet Exp $
   ------------------------------------------------------------

   Volume manager skeleton.
   Philippe Marquet, october 2002

   1- you must complete the NYI (not yet implemented) functions
   2- you may add commands (format, etc.)

*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "drive.h"
#include "mbr.h"
#include "vol.h"
#include "hwinfo.h"
#include "hardware.h"

/* ------------------------------
   command list
   ------------------------------------------------------------*/
struct _cmd {
	char *name;
	void (*fun) (struct _cmd *c);
	char *comment;
};

static void list(struct _cmd *c);
static void new(struct _cmd *c);
static void del(struct _cmd *c);
static void help(struct _cmd *c) ;
static void save(struct _cmd *c);
static void quit(struct _cmd *c);
static void xit(struct _cmd *c);
static void none(struct _cmd *c) ;

static struct _cmd commands [] = {
	{"list", list, 	"display the partition table"},
	{"new",  new,	"create a new partition"},
	{"del",  del,	"delete a partition"},
	{"save", save,	"save the MBR"},
	{"quit", quit,	"save the MBR and quit"},
	{"exit", xit,	"exit (without saving)"},
	{"help", help,	"display this help"},
	{0,	 none,	"unknown command, try help"}
} ;

/* ------------------------------
   dialog and execute 
   ------------------------------------------------------------*/

static void execute(const char *name) {
	struct _cmd *c = commands; 

	while (c->name && strcmp (name, c->name))
		c++;
	(*c->fun)(c);
}

static void loop(void) {
	char name[64];

	while (printf("> "), scanf("%62s", name) == 1)
		execute(name) ;
}

int read_int() {
	int num = 0;
	int ret = 0;
	while (!ret){
		printf("# ");
		fflush(stdout);
		ret = scanf("%d%*[^\n]", &num);
		if (!ret){
			int c;
			while (((c = getchar()) != '\n') && c != EOF);
		}
		else
			getchar();
	}
	return num;
}

/* ------------------------------
   command execution 
   ------------------------------------------------------------*/
static void list(struct _cmd *c) {
	int i;
	printf("** Partitions information **\n"); 
	printf("%d partition%s on HDA\n", mbr.mbr_nvol, (mbr.mbr_nvol > 1 ? "s" : ""));
	for(i = 0; i < mbr.mbr_nvol; i++) {
		struct vol_s vol = mbr.mbr_vols[i];
		printf("Partition %d\n", i);
		printf("\tType : %d\n", vol.vol_type);
		printf("\tSize : %d bytes (%d blocks)\n", (vol.vol_nbloc * HDA_SECTORSIZE), vol.vol_nbloc);
		printf("\tFirst sector : %d\n", sector_of_bloc(i, 0));
		printf("\tFirst cylinder : %d\n", cylinder_of_bloc(i, 0));
		printf("\tLast sector : %d\n", sector_of_bloc(i, vol.vol_nbloc - 1));
		printf("\tLast cylinder : %d\n", cylinder_of_bloc(i, vol.vol_nbloc - 1));
	}
}

static void new(struct _cmd *c) {
	struct vol_s* pvol;
	int i, type, fsector, fcylinder, size, confirm;
	int lsector, lcylinder;
	type = fsector = fcylinder = size = confirm = -1;
	printf("** Create a partition **\n"); 
	if (mbr.mbr_nvol == MAX_VOL) {
		fprintf(stderr, "Error[%s], can't create more partition\n", c->name);
		return;
	}
	printf("Type of partition (0=base, 1=annex, 2=other) ?\n");
	while(type > 2 || type < 0)
		type = read_int();
	printf("First sector ?\n");
	while(fsector < 0 || fsector >= HDA_MAXSECTOR)
		fsector = read_int();
	printf("First cylinder ?\n");
	while(fcylinder < 0 || fcylinder >= HDA_MAXCYLINDER)
		fcylinder = read_int();
	printf("size (blocks) ?\n");
	while(size < 1 || size > HDA_MAXSECTOR * HDA_MAXCYLINDER)
		size = read_int();
	printf("Create a partition of %d blocks from (s:%d,c:%d) and type %d ? (y=1 or n=0)\n", size, fsector, fcylinder, type);
	while(confirm < 0 || confirm > 1)
		confirm = read_int();
	if (confirm == 0) { 
		printf("Partition creation cancelled\n");
		return;
	}
	if (fsector == 0 && fcylinder == 0) {
		fprintf(stderr, "Error[%s], writing on sector 0 and cylinder 0 is forbidden (MBR)\n", c->name);
		return;
	}
	pvol = mbr.mbr_vols + mbr.mbr_nvol;
	pvol->vol_cylinder = fcylinder;
	pvol->vol_sector = fsector;
	pvol->vol_nbloc = size;
	pvol->vol_type = type;
	lsector = sector_of_bloc(mbr.mbr_nvol, mbr.mbr_vols[mbr.mbr_nvol].vol_nbloc - 1);
	lcylinder = cylinder_of_bloc(mbr.mbr_nvol, mbr.mbr_vols[mbr.mbr_nvol].vol_nbloc - 1);
	for(i = 0; i < mbr.mbr_nvol; i++) {
		struct vol_s vol = mbr.mbr_vols[i];
		int fs = sector_of_bloc(i, 0);
		int fc = cylinder_of_bloc(i, 0);
		int ls = sector_of_bloc(i, vol.vol_nbloc - 1);
		int lc = cylinder_of_bloc(i, vol.vol_nbloc - 1);
		if (
			(fcylinder > fc && fcylinder < lc) ||
			(fcylinder == lc && fcylinder > fc && fsector <= ls) ||
			(fcylinder == fc && fcylinder < lc && fsector >= fs) ||
			(fcylinder == fc && fcylinder == lc && fsector >= fs && fsector <= ls) ||
			(lcylinder > fc && lcylinder < lc) ||
			(lcylinder == lc && lcylinder > fc && lsector <= ls) ||
			(lcylinder == fc && lcylinder < lc && lsector >= fs) ||
			(lcylinder == fc && lcylinder == lc && lsector >= fs && lsector <= ls)
		) {
			fprintf(stderr, "Error[%s], create a new partition over another one (partition %d)\n", c->name, i);
			return;
		}
	}
	mbr.mbr_nvol++;
	printf("Partition %d created successfully\n", mbr.mbr_nvol); 
}

static void del(struct _cmd *c) { /* OK */
	int i, vol, confirm;
       	vol = confirm = -1;
	printf("** Delete a partition **\n"); 
	if (mbr.mbr_nvol == 0) {
		fprintf(stderr, "Error[%s], no partition to delete\n", c->name);
		return;
	}
	printf("Partition to delete ?\n");
	while(vol < 0 || vol >= mbr.mbr_nvol)
		vol = read_int();
	printf("Delete the partition %d ? (y=1 or n=0)\n", vol);
	while(confirm < 0 || confirm > 1) 
		confirm = read_int();
	if (confirm == 0) { 
		printf("Partition suppression cancelled\n");
		return;
	}
	for(i = vol; i < mbr.mbr_nvol; i++)
		mbr.mbr_vols[i] = mbr.mbr_vols[i + 1];
	mbr.mbr_nvol--;
	printf("Partition %d deleted successfully\n", vol);
}

static void save(struct _cmd *c) { /* OK */
	printf("** Save the MBR **\n"); 
	printf("Saving the MBR... ");
	save_mbr();
	printf("ok\n");
	printf("MBR saved successfully\n"); 
}

static void quit(struct _cmd *c) { /*OK */
	execute("save");
	printf("Exiting volumes manager\n"); 
	exit(EXIT_SUCCESS);
}

static void do_xit() {
	printf("Exiting volumes manager\n"); 
	exit(EXIT_SUCCESS);
}

static void xit(struct _cmd *dummy) {
	do_xit(); 
}

static void help(struct _cmd *dummy) {
	struct _cmd *c = commands;

	for (; c->name; c++) 
		printf ("%s\t-- %s\n", c->name, c->comment);
}

static void none(struct _cmd *c) {
	printf ("%s\n", c->comment);
}

int main(int argc, char **argv) {

	/* Initialize the harware and load the MBR */
	printf("Initializing volumes manager... ");
	fflush(stdout);
	if (!init_master("hardware.ini"))
		exit(EXIT_FAILURE);
	printf("ok\n");
	printf("Loading HDA MBR... ");
	fflush(stdout);
	if (load_mbr())
		printf("ok\n");
	printf("* You can try command help to know how use vm\n");

	/* dialog with user */ 
	loop();

	/* abnormal end of dialog (cause EOF for xample) */
	do_xit();

	/* make gcc -W happy */
	exit(EXIT_SUCCESS);
}
