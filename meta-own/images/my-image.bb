SUMMARY = "A basic Qt5 qwidgets dev image"

IMAGE_FEATURES += "package-management ssh-server-dropbear"

FEATURE_PACKAGES_ssh-server-dropbear = "packagegroup-core-ssh-dropbear"
FEATURE_PACKAGES_ssh-server-openssh = "packagegroup-core-ssh-openssh"
IMAGE_FEATURES_REPLACES_ssh-server-openssh = "ssh-server-dropbear"
MACHINE_HWCODECS ??= ""

DISTRO_FEATURES_remove = "bluetooth"
DISTRO_FEATURES_remove = "3g"
DISTRO_FEATURES_remove = "opengl"
DISTRO_FEATURES_remove = "wayland"
DISTRO_FEATURES_remove = "x11"
DISTRO_FEATURES_remove = "nfc"
DISTRO_FEATURES_remove = "nfs"
DISTRO_FEATURES_remove = "ext2"
MACHINE_FEATURES_remove = "alsa"
MACHINE_FEATURES_remove = "phone"

DISTRO_FEATURES_append = " ipv4 ipv6"

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
cat <<EOT >> ${IMAGE_ROOTFS}/etc/network/interfaces
auto lo
iface lo inet loopback

auto eth0
iface eth0 inet dhcp
EOT
}
enable_root_login() {
echo "DROPBEAR_EXTRA_ARGS=''" > ${IMAGE_ROOTFS}/etc/default/dropbear
}

ROOTFS_POSTPROCESS_COMMAND += " \
    change_networking ; \
    enable_root_login ; \
"


export IMAGE_BASENAME = "my-image"
