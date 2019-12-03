Nessie
======

An NES emulator written in Scala.


The AS64 Assembler
------------------

This project uses the AS64 assembler for testing purposes. Although this tools is mentioned in many places on the web
as a great free 6502 assembler, I had trouble finding the binary (source doesn't seem to be available at all). I finally
found it via the `Wayback Machine`_. It is included in the ``tools`` folder. It is a 32-bit executable. To get it to
run on my 64-bit arch machine I had to do the following:

#. Uncomment the ``[multilib]`` section in ``/etc/pacman.conf``, then run ``pacman -Syu``.
#. Run ``pacman -S lib32-glibc lib32-gcc-libs``

Good documentation on the assembly language it supports can be found at http://www.obelisk.me.uk/dev65/as65.html.

.. _Wayback Machine: https://web.archive.org/web/20181030174906/http://www.kingswood-consulting.co.uk/assemblers/as65_142.zip
