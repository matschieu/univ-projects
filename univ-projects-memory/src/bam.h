
/*******************/
/* Matschieu  */
/* L3 info - GR2   */
/* PDC - BAM       */
/* 2008            */
/*******************/

#ifndef __BAM_H__
#define __BAM_H__

#include <stdio.h>

/* STRUCTURE & MACROS DECLARATION */

typedef struct mem_block_st Memseg_t;

/* list link of memory block list
   This memory block has a size of seg_size and begin at seg_start address
   */
struct mem_block_st {
	Memseg_t* next;
	void* seg_start;
	int seg_size;
};

#define LINKTAB_SIZE 1024
#define DEF_SIZE 10
#define COEFF 2
/*
#define malloc(sz) bam_malloc(sz, __FILE__, __LINE__)
#define calloc(nb,sz) bam_calloc(nb, sz, __FILE__, __LINE__)
#define realloc(ptr,sz) bam_realloc(ptr, sz, __FILE__, __LINE__)
#define free(ptr) bam_free(ptr, __FILE__, __LINE__)
*/
void init_rand(); __attribute__ ((constructor))

/* MEMORY SEGMENT FUNCTIONS */

/* Return the first unused list link.
   If no link is available, the function return a NULL pointer
   */
Memseg_t* get_memseg();

/* Create and return a new memory block
*/
Memseg_t* init_link(Memseg_t* next, int size);

/* Clear the list link ptr.
   ptr may be used later.
   */
void clear_link(Memseg_t* ptr);

/* MALLOC & CALLOC FUNCTIONS */

/* Return a pointer on a memory block of nmemb member with a size of size.
   The array allocated is initialize each cell with 0 value.
   */
void* bam_calloc(size_t nmemb, size_t size, char* filename, unsigned line);

/* Return a pointer on a memory block of a size size.
   If no memory block is available in the list of free memory block, a new block is created.
   If there are no free blocks compatible, a new block is created to be used
   If it's not possible to create or allocate a free block, the function returns a NULL pointer.
   */
void* bam_malloc(size_t size, char* filename, unsigned line);

/* Return the first free memory block compatible with the allocation request 
   (the free block size is greater than or equal to size).
   The function return a NULL pointer if no block is compatible.
   */
void* firstfit(size_t size);

/* A a free memory block to used blocks list.
   This block has a size of size and begin at start address.
   */
void add_to_used_block(void* start, int size);

/* REALLOC FUNCTION */

/* Return a new pointer on a memory block of a new size size.
   All values contains in the array ptr are copied in the new array.
   */
void* bam_realloc(void* ptr, size_t size, char* filename, unsigned line);

/* FREE FUNCTION */

/* Free a pointer ptr.
   If ptr was'nt allocated by bam_malloc or bam_calloc, the program is stopped on an error.
   Else, the memory block is removed from the used blocks list and add to the free blocks list.
   If it's possible, the memory block is sticked again to an existing free block.
   */
void bam_free(void* ptr, char* filename, unsigned line);

/* DISPLAY MEMORY STATE FUNCTION */

/* Display an invalid pointer error (used by free and realloc).
   Exit the program on an error.
*/
void ip_error_display(void* ptr, char* function, char* filename, unsigned line);

/* Display the state of the memory heap
*/
void memory_display();

#endif
