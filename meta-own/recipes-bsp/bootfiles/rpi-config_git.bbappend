do_deploy_append() {

    cat <<EOT >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt

## MY ADDED ## 
# Disable the rainbow splash screen
disable_splash=1

# Disable bluetooth
dtoverlay=pi3-disable-bt

#Disable Wifi
dtoverlay=pi3-disable-wifi
 
# Overclock the SD Card from 50 to 100MHz
# This can only be done with at least a UHS Class 1 card
# dtoverlay=sdtweak,overclock_50=100
 
# Set the bootloader delay to 0 seconds. The default is 1s if not specified.
boot_delay=0

# Overclock the raspberry pi. This voids its warranty. Make sure you have a good power supply.
# force_turbo=1

EOT
}
