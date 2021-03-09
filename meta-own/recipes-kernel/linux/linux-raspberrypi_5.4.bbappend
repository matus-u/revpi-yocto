FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://defconfig"
CMDLINE_append = "rw lpj=3489792"
