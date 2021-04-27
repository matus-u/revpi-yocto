if [ "$#" != "1" ]; then
  echo 'Wrong argument count, use only "display" or "no-display"'
  return
fi

if [[ "$1" != "display" && "$1" != "no-display" ]]; then
  echo 'Wrong argument, use only "display" or "no-display"'
  return
fi

source poky/poky-dunfell/oe-init-build-env build
cp ../meta-own/conf/local.conf.sample conf/local.conf
cp ../meta-own/conf/bblayers.conf.sample conf/bblayers.conf
sed -i "s|HOME|$PWD/..|" conf/bblayers.conf


if [[ "$1" == "display" ]]; then
  export MACHINE=raspberrypi-cm
  export OETMP=$PWD/tmp
  return
fi

if [[ "$1" == "no-display" ]]; then
  export MACHINE=raspberrypi-cm-no-display
  export OETMP=$PWD/tmp
  return
fi
