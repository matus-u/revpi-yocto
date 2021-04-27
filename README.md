# revpi-yocto

For build:
```bash
git submodule update --init --recursive
. create-build.sh display  # or . create-build.sh no-display - based on board env
bitbake my-image


For flashing insert SD, for setup interfaces edit file in meta-own/scripts/interfaces
Preparation step for copying the system to the eMMC until rpiboot runs. Check on: https://jumpnowtek.com/rpi/Working-with-the-raspberry-pi-compute.html

```
sudo ./rpiboot  # in usbboot -> check prep step, find correct device
```

On another console find correct device and run:

. create-build.sh display  # or . create-build.sh no-display - based on board env
cd ../meta-own/scripts/
./copy_boot.sh sde # check correct device!
./copy_rootfs.sh sde my # check correct device!
