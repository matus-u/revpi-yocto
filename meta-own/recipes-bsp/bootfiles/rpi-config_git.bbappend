do_deploy_append() {

cat <<EOT > ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
force_turbo=1
initial_turbo=40
enable_uart=1
uart_2ndstage=1
kernel=zImage
disable_overscan=1
disable_splash=1
boot_delay=0
force_eeprom_read=0
#gpu_mem=16
EOT
}

