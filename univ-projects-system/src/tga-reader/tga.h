
/* @author Matschieu */

#ifndef __TGA_H__
#define __TGA_H__

struct Pixel {
	unsigned char b;
	unsigned char g;
	unsigned char r;
	unsigned char c;
};

struct Image {
	unsigned int width;
	unsigned int height;
	struct Pixel* pixels;
};

struct TGA_header {
	unsigned char picture_id;
	unsigned char color_table;
	unsigned char picture_type;
	unsigned char color_table_format[5];
	unsigned short x_origin;
	unsigned short y_origin;
	unsigned short width;
	unsigned short height;
	unsigned char bpp;
	unsigned char descriptor;
};

typedef struct Pixel Pixel_t;
typedef struct Image Image_t;
typedef struct TGA_header TGA_header_t;

void error(char* message1, char* message2);
Pixel_t* get_pixel(Image_t* pic, unsigned int i, unsigned int j);
void set_pixel(Image_t* pic, unsigned int i, unsigned int j, unsigned char r, unsigned char g, unsigned char b);
int read_tga_header(int fd, TGA_header_t *header);
int write_tga_header(int fd, TGA_header_t header);
int read_picture(int fd, Image_t* pic, TGA_header_t header);
int write_picture(int fd, Image_t* pic, TGA_header_t header);
void blue(Image_t pic);
void green(Image_t pic);
void red(Image_t pic);
void blue_grey(Image_t pic);
void green_grey(Image_t pic);
void red_grey(Image_t pic);
void black_white(Image_t pic);
void negative(Image_t pic);

#endif
