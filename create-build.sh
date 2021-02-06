
source poky/poky-dunfell/oe-init-build-env build
cp ../meta-own/conf/local.conf.sample conf/local.conf
cp ../meta-own/conf/bblayers.conf.sample conf/bblayers.conf
sed -i "s|HOME|$PWD/..|" conf/bblayers.conf
