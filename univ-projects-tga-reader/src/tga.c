

/* @author Matschieu */

#include <stdio.h>
#include <stdlib.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include "tga.h"


void error(char* message1, char* message2) {
	fprintf(stderr, "Error : %s %s\n", message1, message2);
	exit(EXIT_FAILURE);
}


Pixel_t* get_pixel(Image_t* pic, unsigned int i, unsigned int j) {
	return &(pic->pixels[i * pic->width + j]);
}

void set_pixel(Image_t* pic, unsigned int i, unsigned int j, unsigned char r, unsigned char g, unsigned char b) {
	pic->pixels[i * pic->width + j].r = r;
	pic->pixels[i * pic->width + j].g = g;
	pic->pixels[i * pic->width + j].b = b;
}

int read_tga_header(int fd, TGA_header_t *header) {
	return read(fd, header, sizeof(TGA_header_t));
}

int write_tga_header(int fd, TGA_header_t header) {
	return write(fd, &header, sizeof(TGA_header_t));
}

int read_picture(int fd, Image_t* pic, TGA_header_t header) {
	int size = pic->width * pic->height;
	pic->pixels = (Pixel_t*)malloc(size * sizeof(Pixel_t));
	if (!pic->pixels) return -1;
	if (header.bpp == 24) {
		int s = size * (sizeof(Pixel_t) - sizeof(char));
		int i, ret, cpt = 0;
		char* t = (char*)malloc(s * sizeof(char));
		if (!t) return -1;
		ret = read(fd, t, s * sizeof(char));
		for(i = 0; i < size && cpt < s; i++) {
			pic->pixels[i].b = t[cpt++];
			pic->pixels[i].g = t[cpt++];
			pic->pixels[i].r = t[cpt++];
			pic->pixels[i].c = 0;
		}
		free(t);
		return ret;
	}
	if (header.bpp == 32) return read(fd, pic->pixels, size * sizeof(Pixel_t));
	return -1;
}

int write_picture(int fd, Image_t* pic, TGA_header_t header) {
	int size = pic->width * pic->height;
	if (header.bpp == 24) {
		int s = size * (sizeof(Pixel_t) - sizeof(char));
		int i, ret, cpt = 0;
		char* t = (char*)malloc(s * sizeof(char));
		if (!t) return -1;
		for(i = 0; i < size && cpt < s; i++) {
			t[cpt++] = pic->pixels[i].b;
			t[cpt++] = pic->pixels[i].g;
			t[cpt++] = pic->pixels[i].r;
		}
		ret = write(fd, t, s * sizeof(char));
		free(t);
		return ret;
	}
	if (header.bpp == 32) return write(fd, pic->pixels, size * sizeof(Pixel_t));
	return -1;
}







void blue(Image_t pic) {
	int i;
	for(i = 0; i < pic.width * pic.height; i++) {
		Pixel_t* p = &(pic.pixels[i]);
		p->r = p->g = 0;
	}
}

void green(Image_t pic) {
	int i;
	for(i = 0; i < pic.width * pic.height; i++) {
		Pixel_t* p = &(pic.pixels[i]);
		p->r = p->b = 0;
	}
}

void red(Image_t pic) {
	int i;
	for(i = 0; i < pic.width * pic.height; i++) {
		Pixel_t* p = &(pic.pixels[i]);
		p->b = p->g = 0;
	}
}

void blue_grey(Image_t pic) {
	int i;
	for(i = 0; i < pic.width * pic.height; i++) {
		Pixel_t* p = &(pic.pixels[i]);
		if (p->b < (p->r + p->g)) p->r = p->g = p->b = (p->r + p->g + p->b) / 3;
	}
}

void green_grey(Image_t pic) {
	int i;
	for(i = 0; i < pic.width * pic.height; i++) {
		Pixel_t* p = &(pic.pixels[i]);
		if (p->g < (p->r + p->b)) p->r = p->g = p->b = (p->r + p->g + p->b) / 3;
	}
}
void red_grey(Image_t pic) {
	int i;
	for(i = 0; i < pic.width * pic.height; i++) {
		Pixel_t* p = &(pic.pixels[i]);
		if (p->r < (p->g + p->b)) p->r = p->g = p->b = (p->r + p->g + p->b) / 3;
	}
}



void black_white(Image_t pic) {
	int i;
	for(i = 0; i < pic.width * pic.height; i++) {
		Pixel_t* p = &(pic.pixels[i]);
		p->r = p->g = p->b = (p->r + p->g + p->b) / 3;
	}
}

void negative(Image_t pic) {
	int i;
	for(i = 0; i < pic.width * pic.height; i++) {
		Pixel_t* p = &(pic.pixels[i]);
		p->r = ~p->r;
		p->g = ~p->g;
		p->b = ~p->b;
	}
}
