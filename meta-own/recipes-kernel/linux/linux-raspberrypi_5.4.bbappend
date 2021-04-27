FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/${MACHINE}:"
SRC_URI += "file://defconfig"
CMDLINE_append = "rw lpj=3489792"
CMDLINE_append_raspberrypi-cm-no-display = " quiet"
