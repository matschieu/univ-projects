
/*******************/
/* Matschieu  */
/* L3 info - GR2   */
/* PDC - BAM       */
/* 2008            */
/*******************/

#include "bam.h"

void* malloc(size_t size) {
	return bam_malloc(size, (char*)0, 0);
}

void* calloc(size_t nmemb, size_t size) {
	return bam_calloc(nmemb, size, (char*)0, 0);
}

void* realloc(void* ptr, size_t size) {
	return bam_realloc(ptr, size, (char*)0, 0);
}

void free(void* ptr) {
	bam_free(ptr, (char*)0, 0);
}
