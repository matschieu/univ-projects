
#!/bin/bash

if !(test -f if_nfile  &&  test -f if_cfile && test if_pfile && test -f if_dfile); then
	make
fi

echo "= New partition ==============================="
./mknfs
echo

echo "= New file (tree) ============================="
tree | ./if_nfile
inode1=$?
echo
echo "* dfs ****************" 
./dfs
sleep 2
echo

echo "= New file (ls) ==============================="
ls | ./if_nfile
inode2=$?
echo
echo "* dfs ****************"
./dfs
sleep 2
echo

echo "= Copy file ($inode1) ==============================="
./if_cfile $inode1
inode3=$?
echo
echo "* dfs ****************"
./dfs
sleep 2
echo

echo "= Print file ($inode1) =============================="
./if_pfile $inode1
echo
echo "* dfs ****************"
./dfs
sleep 3
echo

echo "= Print file ($inode2) =============================="
./if_pfile $inode2
echo
echo "* dfs ****************"
./dfs
sleep 3
echo

echo "= Print file ($inode3) =============================="
./if_pfile $inode3
echo
echo "* dfs ****************"
./dfs
sleep 3
echo

echo "= Delete file ($inode2) ============================="
./if_dfile $inode2
echo
echo "* dfs ****************"
./dfs
sleep 2
echo

echo "= Delete file ($inode1) ============================="
./if_dfile $inode1
echo
echo "* dfs ****************"
./dfs
sleep 2
echo

echo "= Delete file ($inode3) ============================="
./if_dfile $inode3
echo
echo "* dfs ****************"
./dfs
sleep 2
echo

