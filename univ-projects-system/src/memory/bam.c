
/*******************/
/* Matschieu  */
/* L3 info - GR2   */
/* PDC - BAM       */
/* 2008            */
/*******************/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include "bam.h"

void init_rand() { srand(time(NULL)); }

/* GLOBAL STATIC VARIABLES */

static Memseg_t* free_blocks = NULL;	/* List of memory blocks ready to be allocated */
static Memseg_t* used_blocks = NULL;	/* List of memory blocks allocated by bam_malloc or bam_calloc */
static Memseg_t links[LINKTAB_SIZE];	/* Array of list links */

/* MEMORY SEGMENT FUNCTIONS */

Memseg_t* get_memseg() {
	int i;
	Memseg_t* t = links;
	for(i = 0; i < LINKTAB_SIZE; i++)
		if (!t[i].next && !t[i].seg_start && !t[i].seg_size) return &t[i];
	return NULL;
}

Memseg_t* init_link(Memseg_t* next, int size) {
	Memseg_t* seg = get_memseg();
	seg->next = next;
	seg->seg_start = (void*)sbrk(size);
	seg->seg_size = size;
	return seg;
}

void clear_link(Memseg_t* ptr) {
	ptr->next = ptr->seg_start = NULL;
	ptr->seg_size = 0;
}

/* MALLOC & CALLOC FUNCTIONS */

void* bam_calloc(size_t nmemb, size_t size, char* filename, unsigned line) {
	void* ret = bam_malloc(nmemb * size, filename, line);
	char* p = (char*)ret;
	int i, j;
	for(i = 0; i < nmemb; i++)
		for(j = 0; j < size; j++) *(p + i + j) = 0;
	return ret;
}

void* bam_malloc(size_t size, char* filename, unsigned line) {
	Memseg_t* ret;
	if (size == 0) return NULL;
	if (!free_blocks) {
		int sz = DEF_SIZE * DEF_SIZE;
		if (size > sz) sz = size * COEFF;
		free_blocks = init_link(NULL, sz);
	}
	ret = firstfit(size);
	if (!ret) {
		Memseg_t* c = free_blocks;
		int sz = DEF_SIZE * DEF_SIZE;
		if (size > sz) sz = size * 2;
		while(c->next) c = c->next;
		c->next = init_link(NULL, sz);
		ret = firstfit(size);
	}
	if (ret) {
		char* p = (char*)ret;
		int i;
		for(i = 0; i < size; i++) p[i] = rand() % 10;
	}
	return ret;
}

void* firstfit(size_t size) {
	Memseg_t* prev = NULL;
	Memseg_t* list = free_blocks;
	while(list) {
		void* ret = list->seg_start;
		if (list->seg_size == size) {
			add_to_used_block(list->seg_start, size);
			if (!prev) free_blocks = list->next;
			else prev->next = list->next;
			clear_link(list);
			return ret;
		}
		else if (list->seg_size > size) {
			char* tmp = (char*)list->seg_start + size;
			add_to_used_block(list->seg_start, size);
			list->seg_start = (void*)tmp;
			list->seg_size -= size;
			return ret;
		}
		prev = list;
		list = list->next;
	}
	return NULL;
}

void add_to_used_block(void* start, int size) {
	Memseg_t* seg = get_memseg();
	Memseg_t* list = used_blocks;
	seg->next = NULL;
	seg->seg_start = start;
	seg->seg_size = size;
	if (!used_blocks) used_blocks = seg;
	else {
		while(list->next) list = list->next;
		list->next = seg;
	}
}

/* REALLOC FUNCTION */

void* bam_realloc(void* ptr, size_t size, char* filename, unsigned line) {
	Memseg_t* ulist = used_blocks;
	char* ret = (char*)bam_malloc(size, filename, line);
	char* old = (char*)ptr;
	int old_size, i;
	if (!ptr) return NULL;
	while(ulist) {
		if (ulist->seg_start == ptr) {
			old_size = ulist->seg_size;
			break;
		}
		ulist = ulist->next;
	}
	if (!ulist) ip_error_display(ptr, "bam_realloc", filename, line);
	for(i = 0; i < old_size && i < size; i++) ret[i] = old[i];
	bam_free(ptr, __FILE__, __LINE__);
	return (void*)ret;
}

/* FREE FUNCTION */

void bam_free(void* ptr, char* filename, unsigned line) {
	Memseg_t* prev = NULL;
	Memseg_t* ulist = used_blocks;
	Memseg_t* flist = free_blocks;
	char* padd, * nadd;
	char* p = (char*)ptr;
	int i;
	if (!ptr) return;
	while(ulist) {
		if (ulist->seg_start == ptr) {
			if (prev) prev->next = ulist->next;
			else used_blocks = ulist->next;
			break;
		}
		prev = ulist;
		ulist = ulist->next;
	}
	if (!ulist) ip_error_display(ptr, "bam_free", filename, line);
	for(i = 0; i < ulist->seg_size; i++) p[i] = rand() % 10;
	prev = NULL;
	padd = (char*)ulist->seg_start;
	nadd = (char*)ulist->seg_start + ulist->seg_size;
	while(flist) {
		if (flist->seg_start == nadd) {
			flist->seg_start = ulist->seg_start;
			flist->seg_size += ulist->seg_size;
			break;
		}
		if (flist->seg_start == padd - flist->seg_size) {
			flist->seg_size += ulist->seg_size;
			break;
		}
		prev = flist;
		flist = flist->next;
	}
	if (!flist) {
		if (!prev) free_blocks = ulist;
		else prev->next = ulist;
		ulist->next = NULL;;
	}
	else clear_link(ulist);
}

/* DISPLAY MEMORY STATE FUNCTION */

void ip_error_display(void* ptr, char* function, char* filename, unsigned line) {
	fprintf(stderr, "Error in file %s at line %d\n", filename, line);
	fprintf(stderr, "%s : Invalid pointer 0x%x\n", function, (int)ptr);
	fprintf(stderr, "This pointer was not allocated\n");
	memory_display();
	exit(EXIT_FAILURE);
}

void memory_display() {
	Memseg_t* pt = free_blocks;
	int cpt1 = 0;
	int cpt2 = 0;
	printf("=MEMORY_STATE===================\n");
	printf("* => START ADDRESSES :\n");
	printf("*\tfree_blocks = %x\n", (unsigned int)free_blocks);
	printf("*\tused_blocks = %x\n", (unsigned int)used_blocks);	
	printf("* => FREE BLOCKS :\n");
	while(pt) {
		printf("*\tf[%x | start:%x | size:%d | next:%x]\n", 
				(unsigned int)pt, (unsigned int)pt->seg_start, pt->seg_size, (unsigned int)pt->next);
		pt = pt->next;
		cpt1++;
	}
	printf("*\t#free:%d\n",cpt1);
	pt = used_blocks;
	printf("* => USED BLOCKS :\n");
	while(pt) {
		printf("*\tu[%x | start:%x | size:%d | next:%x]\n", 
				(unsigned int)pt, (unsigned int)pt->seg_start, pt->seg_size, (unsigned int)pt->next);
		pt = pt->next;
		cpt2++;
	}
	printf("*\t#used:%d\n",cpt2);
	printf("================================\n");
}
