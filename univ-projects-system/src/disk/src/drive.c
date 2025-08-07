
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "hardware.h"
#include "hwinfo.h"
#include "drive.h"

static void empty_it() {
}

int init_master(const char* ini_file) {
	int i;
	if (!init_hardware(ini_file)) {
		fprintf(stderr, "Error[%s]: hardware initialization (%s)\n", __FUNCTION__, ini_file);
		return 0;
	}
	for(i = 0; i < 16; i++)
		IRQVECTOR[i] = empty_it;
	_mask(1);
	return 1;
}

static void goto_sector(unsigned int cylinder, unsigned int sector) {
	if (cylinder >= HDA_MAXCYLINDER || sector >= HDA_MAXSECTOR) {
		fprintf(stderr, "Error[%s]: invalid cylinder/sector (%d, %d)\n", __FUNCTION__, cylinder, sector);
		chk_geometry();
		exit(EXIT_FAILURE);
	}
	_out(HDA_DATAREGS, (cylinder >> 8) & 0xff);
	_out(HDA_DATAREGS + 1, cylinder & 0xff);
	_out(HDA_DATAREGS + 2, (sector >> 8) & 0xff);
	_out(HDA_DATAREGS + 3, sector & 0xff);
	_out(HDA_CMDREG, CMD_SEEK);
	_sleep(HDA_IRQ);
}

void read_sector(unsigned int cylinder, unsigned int sector, unsigned char* buffer) {
	goto_sector(cylinder, sector);
	_out(HDA_DATAREGS, 0);
	_out(HDA_DATAREGS + 1, 1);
	_out(HDA_CMDREG, CMD_READ);
	_sleep(HDA_IRQ);
	memcpy(buffer, MASTERBUFFER, HDA_SECTORSIZE);
}

void read_sector_n(unsigned int cylinder, unsigned int sector, unsigned char* buffer, unsigned int buf_size) {
	goto_sector(cylinder, sector);
	_out(HDA_DATAREGS, 0);
	_out(HDA_DATAREGS + 1, 1);
	_out(HDA_CMDREG, CMD_READ);
	_sleep(HDA_IRQ);
	if (buf_size > HDA_SECTORSIZE)
		buf_size = HDA_SECTORSIZE;
	memcpy(buffer, MASTERBUFFER, buf_size);
}

void write_sector(unsigned int cylinder, unsigned int sector, const unsigned char* buffer) {
	int buf_size = strlen((char*)buffer);
	goto_sector(cylinder, sector);
	if (buf_size > HDA_SECTORSIZE)
		buf_size = HDA_SECTORSIZE;
	memcpy(MASTERBUFFER, buffer, buf_size);
	_out(HDA_DATAREGS, 0);
	_out(HDA_DATAREGS + 1, 1);
	_out(HDA_CMDREG, CMD_WRITE);
	_sleep(HDA_IRQ);
}

void write_sector_n(unsigned int cylinder, unsigned int sector, const unsigned char* buffer, unsigned int buf_size) {
	goto_sector(cylinder, sector);
	if (buf_size > HDA_SECTORSIZE)
		buf_size = HDA_SECTORSIZE;
	memcpy(MASTERBUFFER, buffer, buf_size);
	_out(HDA_DATAREGS, 0);
	_out(HDA_DATAREGS + 1, 1);
	_out(HDA_CMDREG, CMD_WRITE);
	_sleep(HDA_IRQ);
}

void format_sector(unsigned int cylinder, unsigned int sector, unsigned int nsector, unsigned int value) {
	int i;
	goto_sector(cylinder, sector);
	_out(HDA_DATAREGS, (nsector >> 8) & 0xff);
	_out(HDA_DATAREGS + 1, nsector & 0xff);
	_out(HDA_DATAREGS + 2, (value >> 24) & 0xff);
	_out(HDA_DATAREGS + 3, (value >> 16) & 0xff);
	_out(HDA_DATAREGS + 4, (value >> 8) & 0xff);
	_out(HDA_DATAREGS + 5, value & 0xff);
	_out(HDA_CMDREG, CMD_FORMAT);
	for(i = 0; i < nsector; i++)
		_sleep(HDA_IRQ);
}

void chk_geometry() {
	int nb_cylinder;
	int nb_sector;
	int sector_size;
	printf("** DISK INFO **\n");
	_out(HDA_CMDREG, CMD_DSKINFO);
	nb_cylinder = ((_in(HDA_DATAREGS) << 8) & 0xff00) | (_in(HDA_DATAREGS + 1) & 0xff);
	nb_sector = ((_in(HDA_DATAREGS + 2) << 8) & 0xff00) | (_in(HDA_DATAREGS + 3) & 0xff);
	sector_size = ((_in(HDA_DATAREGS + 4) << 8) & 0xff00) | (_in(HDA_DATAREGS + 5) & 0xff);
	printf(" #cylinders: %d\n", nb_cylinder);
	printf(" #sectors: %d\n", nb_sector);
	printf(" sector size: %d bytes\n", sector_size);
}

