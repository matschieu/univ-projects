#include "window.h"

#include <string.h>
#include <strings.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>

  /*Coordonnees de la fenetre courante*/
  int winX,winY, winHeight,winWidth;
  /*Definition de la communication serveur */
  Display * display;
  /*Definition de la fenetre courante*/
  Window win;
  /*Table de couleur courante*/
  Colormap clm;
  /*Definition du contexte graphique courant*/
  GC graphic_context;
  /*Sauvegarde du fond*/
  Pixmap Screen_Save;
  /*Image pour la sauvegarde*/
  XImage Screen_Mem;
  /*Definition des polices utilisees*/
  XFontStruct * fd;
  char BitDepth;
  XGCValues gcv;


int createWindow(int x, int y, int width, int height) {
  XEvent e;
  XSetWindowAttributes xsw;
  XSizeHints hint;
  XWMHints Shint;

  hint.x = x;
  hint.y = y;
  Shint.initial_state = InactiveState;
  hint.width = width;
  hint.height = height;
  hint.flags = PPosition | PSize;
  Shint.flags = StateHint;
  winX = x;
  winY = y;
  winHeight = height;
  winWidth = width;
  if ((display = XOpenDisplay (getenv ("DISPLAY"))) == NULL) {
    fprintf(stderr,"Impossible de contacter le serveur graphique\n");
    return -1;
  }
  BitDepth = DefaultDepth (display, DefaultScreen (display));
  Screen_Save = XCreatePixmap (display, RootWindow (display, DefaultScreen (display)),
                               width, height, BitDepth);
  xsw.background_pixmap = Screen_Save;
  xsw.border_pixel = BlackPixel (display, DefaultScreen (display));

  win = XCreateWindow (display, RootWindow (display, DefaultScreen (display)),
                       x, y, width, height, 2, BitDepth, InputOutput,
                       CopyFromParent, CWBackPixmap, &xsw);
  if ((fd = XLoadQueryFont (display, "fixed")) == NULL) {
    fprintf(stderr,"Impossible de charger la police fixed\n");
    return -2;
  }
  gcv.font = fd->fid;
  gcv.foreground = 0xffffff;
  gcv.background = 0;
  graphic_context = XCreateGC (display, win, GCFont | GCForeground | GCBackground, &gcv);

  XSetStandardProperties (display, win, "Projet N3",
                          "Projet N3", None, NULL, 0, &hint);
  XSetWMHints (display, win, &Shint);
  XMapWindow (display, win);
  XSelectInput (display, win, ExposureMask);
  bzero(&e,sizeof(e));
  XNextEvent (display, &e);
  XSync (display, 0);

  clearGraph ();

  return 0;
}

void destroyWindow() {
  XFreeFont (display, fd);
  XFreePixmap (display, Screen_Save);
  XDestroyWindow (display, win);
  XCloseDisplay (display);
}


void putPixel (int x, int y,unsigned char red,unsigned char green,unsigned char blue) {
  long color= (red<<16) | (green<<8) | blue;
  XSetForeground (display, graphic_context, color);
  XDrawPoint (display, Screen_Save, graphic_context, x, y);
  XDrawPoint (display, win, graphic_context, x, y);
}

void clearGraph () {
  XSetForeground (display, graphic_context, 0);
  /* XFillRectangle (display, Screen_Save, graphic_context, 0, 0, winWidth,winHeight); */
  XFillRectangle (display, win, graphic_context, 0, 0, winWidth,winHeight);
  XSetForeground (display, graphic_context, 0);
  /* XClearWindow (display, win); */
  XSync (display, 0);
}

void redrawWindow () {
  XCopyArea (display, Screen_Save, win, graphic_context, 0, 0, winWidth,
             winHeight, 0, 0);
  XRaiseWindow (display, win);
  XSync (display, 0);
}

