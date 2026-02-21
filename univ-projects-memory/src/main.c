
/*******************/
/* Matschieu  */
/* L3 info - GR2   */
/* PDC - BAM       */
/* 2008            */
/*******************/

#include <stdio.h>
#include <stdlib.h>
#include "bam.h"

/* #define DEBUG
*/

int main(int argc, char** argv) {
	char* a, * b, * c;
	int* d, * e;
	int i, t = 0;
	a = b = c = 0;
	d = e = 0;
	
	/* Allocation des tableaux */
	
	printf("** Array allocation (malloc and calloc) **\n\n");
	
	t = 100 * sizeof(char);
	printf("Trying to allocate a with malloc (%d bytes)... ", t);
	a = (char*)malloc(t);
	if (a) printf("OK\nMalloc returned 0x%x\n\n", (int)a);
	else printf("ERROR\n\n");
	#ifdef DEBUG
	memory_display();
	printf("\n");
	#endif
	
	t = 1000 * sizeof(char);
	printf("Trying to allocate b with malloc (%d bytes)... ", t);
	b = (char*)malloc(t);
	if (b) printf("OK\nMalloc returned 0x%x\n\n", (int)b);
	else printf("ERROR\n\n");
	#ifdef DEBUG
	memory_display();
	printf("\n");
	#endif
	
	t = 500 * sizeof(char);
	printf("Trying to allocate c with malloc (%d bytes)... ", t);
	c = (char*)malloc(t);
	if (c) printf("OK\nMalloc returned 0x%x\n\n", (int)c);
	else printf("ERROR\n\n");
	#ifdef DEBUG
	memory_display();
	printf("\n");
	#endif
	
	t = 500 * sizeof(int);
	printf("Trying to allocate d with calloc (%d bytes)... ", t);
	d = (int*)calloc(500, sizeof(int));
	if (d) printf("OK\ncalloc returned 0x%x\n\n", (int)d);
	else printf("ERROR\n\n");
	#ifdef DEBUG
	memory_display();
	printf("\n");
	#endif
	
	t = 250 * sizeof(int);
	printf("Trying to allocate e with calloc (%d bytes)... ", t);
	e = (int*)calloc(250, sizeof(int));
	if (e) printf("OK\ncalloc returned 0x%x\n\n", (int)e);
	else printf("ERROR\n\n");
	#ifdef DEBUG
	memory_display();
	printf("\n");
	#endif
	
	/* Affichage des 10 premières valeurs non initialisées de chaque tableau */
	
	printf("** Display 10 values not initilized for each array **\n\n");

	printf("a[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", a[i]);
	printf("]\n");
	
	printf("b[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", b[i]);
	printf("]\n");
	
	printf("c[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", c[i]);
	printf("]\n");
	
	printf("d[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", d[i]);
	printf("]\n");

	printf("e[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", e[i]);
	printf("]\n\n");
	
	/* Initialisation des tableaux */
	
	for(i = 0; i < 10; i++) a[i] = i;
	for(i = 0; i < 10; i++) b[i] = i;
	for(i = 0; i < 10; i++) c[i] = i;
	for(i = 0; i < 10; i++) d[i] = i;
	for(i = 0; i < 10; i++) e[i] = i;
	
	/* Affichage des 10 premières valeurs initialisées de chaque tableau */
	
	printf("** Display 10 values initilized for each array **\n\n");
	
	printf("a[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", a[i]);
	printf("]\n");
	
	printf("b[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", b[i]);
	printf("]\n");
	
	printf("c[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", c[i]);
	printf("]\n");
	
	printf("d[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", d[i]);
	printf("]\n");

	printf("e[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", e[i]);
	printf("]\n\n");
	
	
	
	/* Libération des tableaux */
	
	printf("** Free each array (free) **\n\n");
	
	if (b) {
		printf("Trying to free b... ");
		free(b);
		printf("OK\n\n");
		#ifdef DEBUG
		memory_display();
		printf("\n");
		#endif
	}
	
	if (e) {
		printf("Trying to free e... ");
		free(e);
		printf("OK\n\n");
		#ifdef DEBUG
		memory_display();
		printf("\n");
		#endif
	}
	
	/* Réallocation de tableaux */	
	
	printf("** Array reallocation (realloc) **\n\n");
	
	t = 500 * sizeof(char);
	printf("Trying to reallocate a with realloc (%d bytes)... ", t);
	a = (char*)realloc(a, t);
	if (a) printf("OK\nRealloc returned 0x%x\n\n", (int)a);
	else printf("ERROR\n\n");
	#ifdef DEBUG
	memory_display();
	printf("\n");
	#endif

	t = 700 * sizeof(int);
	printf("Trying to reallocate d with realloc (%d bytes)... ", t);
	d = (int*)realloc(d, t);
	if (d) printf("OK\nRealloc returned 0x%x\n\n", (int)d);
	else printf("ERROR\n\n");
	#ifdef DEBUG
	memory_display();
	printf("\n");
	#endif
	
	/* Affichage des 10 premières valeurs non initialisées de chaque tableau */
	
	printf("** Display 10 values not initilized for each array **\n\n");
	
	printf("a[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", a[i]);
	printf("]\n");
	
	printf("d[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", d[i]);
	printf("]\n\n");
	
	/* Libération des tableaux */
	
	printf("** Free each array (free) **\n\n");
	
	if (a) {
		printf("Trying to free a... ");
		free(a);
		printf("OK\n\n");
		#ifdef DEBUG
		memory_display();
		printf("\n");
		#endif
	}
	
	if (c) {
		printf("Trying to free c... ");
		free(c);
		printf("OK\n\n");
		#ifdef DEBUG
		memory_display();
		printf("\n");
		#endif
	}
	
	if (d) {
		printf("Trying to free d... ");
		free(d);
		printf("OK\n\n");
		#ifdef DEBUG
		memory_display();
		printf("\n");
		#endif
	}
	
	/* Affichage des tableaux après libération */
	
	printf("** Display 10 values for each array after free **\n\n");
	
	printf("a[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", a[i]);
	printf("]\n");
	
	printf("b[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", b[i]);
	printf("]\n");
	
	printf("c[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", c[i]);
	printf("]\n");
	
	printf("d[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", d[i]);
	printf("]\n");

	printf("e[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", e[i]);
	printf("]\n\n");
	
	/* Test de réallocation */
	
	printf("** Array allocation (malloc) **\n\n");
	
	t = 100 * sizeof(char);
	printf("Trying to allocate a with malloc (%d bytes)... ", t);
	a = (char*)malloc(t);
	if (a) printf("OK\nMalloc returned 0x%x\n\n", (int)a);
	else printf("ERROR\n\n");
	#ifdef DEBUG
	memory_display();
	printf("\n");
	#endif
	
	for(i = 0; i < 10; i++) a[i] = i;
	
	printf("** Array reallocation (realloc) **\n\n");
	
	t = 500 * sizeof(char);
	printf("Trying to reallocate a with realloc (%d bytes)... ", t);
	a = (char*)realloc(a, t);
	if (a) printf("OK\nRealloc returned 0x%x\n\n", (int)a);
	else printf("ERROR\n\n");
	#ifdef DEBUG
	memory_display();
	printf("\n");
	#endif

	printf("** Display 10 values initilized for this array **\n\n");
	
	printf("a[i] = [");
	for(i = 0; i < 10; i++) printf("%d, ", a[i]);
	printf("]\n\n");
	
	printf("** Free this array (free) **\n\n");
	
	if (a) {
		printf("Trying to free a... ");
		free(a);
		printf("OK\n\n");
		#ifdef DEBUG
		memory_display();
		printf("\n");
		#endif
	}
	
	printf("** Free a not allocated array (free) **\n\n");
	
	printf("Trying to free a...\n\n");
	free(a);
	
	return EXIT_SUCCESS;
}
