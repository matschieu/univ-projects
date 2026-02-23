#include "targa.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

typedef unsigned char byte;

#pragma pack(push,1)
typedef struct {
  byte  identsize;          /* size of ID field that follows 18 byte header (0 usually) */
  byte  colourmaptype;      /* type of colour map 0=none, 1=has palette */
  byte  imagetype;          /* type of image 0=none,1=indexed,2=rgb,3=grey,+8=rle packed */

  short colourmapstart;     /* first colour map entry in palette */
  short colourmaplength;    /* number of colours in palette */
  byte  colourmapbits;      /* number of bits per palette entry 15,16,24,32 */

  short xstart;             /* image x origin */
  short ystart;             /* image y origin */
  short width;              /* image width in pixels */
  short height;             /* image height in pixels */
  byte  bits;               /* image bits per pixel 8,16,24,32 */
  byte  descriptor;         /* image descriptor bits (vh flip bits) */
  /* descriptor: 00vhaaaa */
  /*        - h horizontal flip */
  /*        - v vertical flip */
  /*        - a alpha bits */

  /* pixel data follows header */
    
} TGA_HEADER;
#pragma pack(pop)

int loadTGA(char* filename, PIXEL img[512][512]) {
  int f=open(filename,O_RDONLY), n;
  unsigned char buf[1];
  TGA_HEADER header;
  int x,y;
  if (-1 == f) return 0; /* erreur d'ouverture */
  n = read(f,&header,18);
  if ( n != 18 ) return 0; /* erreur lecture */

  if (
      (header.identsize != 0) ||
      (header.colourmaptype != 0) ||     /* type of colour map 0=none, 1=has palette */
      (header.imagetype != 2) ||          /* type of image 0=none,1=indexed,2=rgb,3=grey,+8=rle packed */

      (header.colourmapstart != 0) ||     /* first colour map entry in palette */
      (header.colourmaplength != 0) ||    /* number of colours in palette */
      (header.colourmapbits != 0) ||      /* number of bits per palette entry 15,16,24,32 */

      (header.xstart != 0) ||             /* image x origin */
      (header.ystart != 0) ||             /* image y origin */
      (header.width != 512) ||              /* image width in pixels */
      (header.height != 512) ||             /* image height in pixels */
      ((header.bits != 24) && (header.bits != 32)) ||               /* image bits per pixel */
      ((header.descriptor != 32) && (header.descriptor != 40))         /* image descriptor bits (vh flip bits) */
      ) {
    return 0; /* mauvais format d'image */
  }

  for (y=0;y<512;++y) {
    for (x=0;x<512;++x) {
      n = read(f,&img[x][y].b,1);
      if ( n != 1 ) return 0; /* erreur lecture */
      n = read(f,&img[x][y].g,1);
      if ( n != 1 ) return 0; /* erreur lecture */
      n = read(f,&img[x][y].r,1);
      if ( n != 1 ) return 0; /* erreur lecture */
      if (header.bits == 32) {
        n = read(f,buf,1); /* RGBA ou RGB */
        if ( n != 1 ) return 0; /* erreur lecture */
      }
    }
  }
  close(f);
  return 1;
}

int saveTGA(PIXEL img[512][512], char* filename) {
  int f=open(filename,O_CREAT|O_WRONLY|O_TRUNC,S_IRUSR|S_IWUSR), n;
  int x,y;
  TGA_HEADER header;
  unsigned char alpha=255;
  if (-1 == f) return 0; /* erreur d'ouverture */
  header.identsize = 0;          /* size of ID field that follows 18 byte header (0 usually) */
  header.colourmaptype = 0;      /* type of colour map 0=none, 1=has palette */
  header.imagetype = 2;          /* type of image 0=none,1=indexed,2=rgb,3=grey,+8=rle packed */

  header.colourmapstart = 0;     /* first colour map entry in palette */
  header.colourmaplength = 0;    /* number of colours in palette */
  header.colourmapbits = 0;      /* number of bits per palette entry 15,16,24,32 */

  header.xstart = 0;             /* image x origin */
  header.ystart = 0;             /* image y origin */
  header.width = 512;              /* image width in pixels */
  header.height = 512;             /* image height in pixels */
  header.bits = 32;               /* image bits per pixel 8,16,24,32 */
  header.descriptor = 40;         /* image descriptor bits (vh flip bits) */

  n = write(f,&header,18);
  if ( n != 18 ) return 0; /* erreur ecriture */
  for (y=0;y<512;++y) {
    for (x=0;x<512;++x) {
      n = write(f,&img[x][y].b,1);
      if ( n != 1 ) return 0; /* erreur ecriture */
      n = write(f,&img[x][y].g,1);
      if ( n != 1 ) return 0; /* erreur ecriture */
      n = write(f,&img[x][y].r,1);
      if ( n != 1 ) return 0; /* erreur ecriture */
      n = write(f,&alpha,1);
      if ( n != 1 ) return 0; /* erreur ecriture */
    }
  }
  close(f);
  return 1;
}
