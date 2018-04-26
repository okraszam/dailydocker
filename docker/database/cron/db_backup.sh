#!/bin/bash
cp -TRf ./etc/mysql ./config_backup
cp -TRf ./var/lib/mysql ./database_backup
cp -TRf. /var/log/mysql ./logs_backup