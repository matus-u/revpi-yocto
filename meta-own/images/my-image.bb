SUMMARY = "A basic Qt5 qwidgets dev image"

IMAGE_FEATURES += "package-management ssh-server-dropbear"

FEATURE_PACKAGES_ssh-server-dropbear = "packagegroup-core-ssh-dropbear"
FEATURE_PACKAGES_ssh-server-openssh = "packagegroup-core-ssh-openssh"
IMAGE_FEATURES_REPLACES_ssh-server-openssh = "ssh-server-dropbear"
MACHINE_HWCODECS ??= ""

inherit image

DEPENDS += "bootfiles"

CORE_OS = " \
    packagegroup-core-boot \
    term-prompt \
"


IMAGE_INSTALL += " \
    ${CORE_OS} \
"

DEV_EXTRAS = " \
    serialecho  \
"

RPI_STUFF = " \
    raspi2fb \
    userland \
"

IMAGE_INSTALL += " \
    ${DEV_EXTRAS} \
    ${RPI_STUFF} \
"

QT_TOOLS = " \
    qtbase \
    qt5-env \
    qtserialport \
    qtbase-plugins \
"

FONTS = " \
    fontconfig \
    fontconfig-utils \
    ttf-bitstream-vera \
"

TSLIB = " \
    tslib \
    tslib-conf \
    tslib-calibrate \
    tslib-tests \
"

TEST_APPS += " \
    qcolorcheck-tools \
    tspress-tools \
"

IMAGE_INSTALL += " \
    ${FONTS} \
    ${QT_TOOLS} \
    ${TEST_APPS} \
    ${TSLIB} \
"

enable_root_login() {
echo "DROPBEAR_EXTRA_ARGS=''" > ${IMAGE_ROOTFS}/etc/default/dropbear
}

prepare_fast_boot() {
rm ${IMAGE_ROOTFS}/etc/rcS.d/S01keymap.sh
rm ${IMAGE_ROOTFS}/etc/rcS.d/S02banner.sh
rm ${IMAGE_ROOTFS}/etc/rcS.d/S02sysfs.sh
rm ${IMAGE_ROOTFS}/etc/rcS.d/S03mountall.sh
rm ${IMAGE_ROOTFS}/etc/rcS.d/S29read-only-rootfs-hook.sh
rm ${IMAGE_ROOTFS}/etc/rcS.d/S06checkroot.sh
rm ${IMAGE_ROOTFS}/etc/rcS.d/S05modutils.sh
rm ${IMAGE_ROOTFS}/etc/rcS.d/S04udev
rm ${IMAGE_ROOTFS}/etc/rc2.d/S01networking
rm ${IMAGE_ROOTFS}/etc/rc2.d/S15mountnfs.sh
rm ${IMAGE_ROOTFS}/etc/rc2.d/S02dbus-1


sed -i "s/id:5:initdefault:/id:2:initdefault:/" ${IMAGE_ROOTFS}/etc/inittab


#CHANGE INIT TO MY SCRIPT
rm ${IMAGE_ROOTFS}/sbin/init
cat <<EOT > ${IMAGE_ROOTFS}/sbin/init
#!/bin/sh

if [ ! -e /dev/tty ]; then
    /bin/mknod -m 0666 /dev/tty c 5 0
fi

if ( > /dev/tty0 ) 2>/dev/null; then
    vtmaster=/dev/tty0
elif ( > /dev/vc/0 ) 2>/dev/null; then
    vtmaster=/dev/vc/0
elif ( > /dev/console ) 2>/dev/null; then
    vtmaster=/dev/console
else
    vtmaster=/dev/null
fi

#echo "Please wait: booting..." > /dev/kmsg
#echo > $vtmaster
#echo "Please wait: booting..." > $vtmaster

#mount -n -t devtmpfs devtmpfs /dev
mkdir /tmp/
mount -t tmpfs tmpfs /tmp

. /etc/default/rcS

mount -at nonfs,nosmbfs,noncpfs 2>/dev/null

#
# We might have mounted something over /dev, see if /dev/initctl is there.
#
if test ! -p /dev/initctl
then
  rm -f /dev/initctl
  mknod -m 600 /dev/initctl p
fi
kill -USR1 1

sysctl -e -p /etc/sysctl.conf >/dev/null 2>&1
ifup eth0

/etc/init.d/S04x-my-app
mount -t sysfs sysfs /sys
exec /sbin/init.sysvinit
EOT
chmod +x ${IMAGE_ROOTFS}/sbin/init


#ADD OWN START APP SCRIPT
cat <<EOT > ${IMAGE_ROOTFS}/etc/init.d/S04x-my-app
#!/bin/sh
echo "START APP" > /dev/kmsg
. /etc/profile.d/qt5-env.sh
tspress &
sleep 10

EOT
chmod +x ${IMAGE_ROOTFS}/etc/init.d/S04x-my-app


cat <<EOT >> ${IMAGE_ROOTFS}/etc/profile.d/qt5-env.sh
export TSLIB_TSDEVICE=/dev/input/event0
export TSLIB_CALIBFILE=/etc/pointercal
export TSLIB_CONFFILE=/etc/ts.conf
export QT_QPA_GENERIC_PLUGINS=tslib:/dev/input/event0
export QT_QPA_EVDEV_TOUCHSCREEN_PARAMETERS=/dev/input/event0
EOT

echo -n "12823 271 -487156 -137 7668 716376 65536 800 480 0" > ${IMAGE_ROOTFS}/etc/pointercal

}




ROOTFS_POSTPROCESS_COMMAND += " \
    enable_root_login ; \
    prepare_fast_boot ; \
"


export IMAGE_BASENAME = "my-image"
