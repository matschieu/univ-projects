
#include <stdio.h>
#include <stdlib.h>
#include "mtools.h"

int get_current_volume() {
    char* current_volume_str;
    current_volume_str = getenv("CURRENT_VOLUME");
    if (!current_volume_str)
        return 1;
    return strtol(current_volume_str, NULL, 10);
}

char* get_hw_config() {
    char* hw_config;
    hw_config = getenv("HW_CONFIG");
    return hw_config ? hw_config : DEFAULT_HW_CONFIG;
}
