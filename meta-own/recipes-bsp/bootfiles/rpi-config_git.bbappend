do_deploy_append() {

cat <<EOT > ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
force_turbo=1
initial_turbo=40
#enable_uart=1
#uart_2ndstage=1
kernel=zImage
disable_overscan=1
disable_splash=1
boot_delay=0
hdmi_group=2
hdmi_mode=87
hdmi_cvt=800 480 60 6 0 0 0
EOT
}


#arm_freq=1000
#arm_freq_min=700
#core_freq=500
#sdram_freq=500
#over_voltage=6
