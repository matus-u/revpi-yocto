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

change_networking() {
cat <<EOT > ${IMAGE_ROOTFS}/etc/network/interfaces
#auto lo
#iface lo inet loopback

auto eth0
iface eth0 inet dhcp
EOT
}

enable_root_login() {
echo "DROPBEAR_EXTRA_ARGS=''" > ${IMAGE_ROOTFS}/etc/default/dropbear
}

prepare_fast_boot() {
rm ${IMAGE_ROOTFS}/etc/rcS.d/S01keymap.sh
rm ${IMAGE_ROOTFS}/etc/rcS.d/S29read-only-rootfs-hook.sh
rm ${IMAGE_ROOTFS}/etc/rcS.d/S06devpts.sh
rm ${IMAGE_ROOTFS}/etc/rcS.d/S06checkroot.sh
rm ${IMAGE_ROOTFS}/etc/rcS.d/S05modutils.sh
rm ${IMAGE_ROOTFS}/etc/rcS.d/S04udev
rm ${IMAGE_ROOTFS}/etc/rc2.d/S15mountnfs.sh
rm ${IMAGE_ROOTFS}/etc/rc2.d/S02dbus-1
cp ${IMAGE_ROOTFS}/etc/init.d/networking ${IMAGE_ROOTFS}/etc/rcS.d/S04networking

sed -i "s/id:5:initdefault:/id:2:initdefault:/" ${IMAGE_ROOTFS}/etc/inittab

cat <<EOT > ${IMAGE_ROOTFS}/etc/rcS.d/S04x-my-app
#!/bin/sh
echo "START APP" > /dev/kmsg
export QT_QPA_PLATFORM=eglfs
/usr/bin/qcolorcheck
EOT
chmod +x ${IMAGE_ROOTFS}/etc/rcS.d/S04x-my-app

cat <<EOT > ${IMAGE_ROOTFS}/etc/rcS.d/S02sysfs.sh
#!/bin/sh
mount -t proc proc /proc
mount -t sysfs sysfs /sys
mount -n -t devtmpfs devtmpfs /dev
mkdir /tmp/
mount -t tmpfs tmpfs /tmp
EOT

}


ROOTFS_POSTPROCESS_COMMAND += " \
    change_networking ; \
    enable_root_login ; \
    prepare_fast_boot ; \
"


export IMAGE_BASENAME = "my-image"
