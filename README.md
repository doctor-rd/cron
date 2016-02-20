# cron
A cron similar to anacron but with some kind of crontab.

## Tools
### crond.py
A cron daemon. It parses the file crontab in its current working directory and stays alive. Type

    # ./crond.py -h

for more information.

### acron.py
On startup it checks its state and runs all pending jobs and exits. It is not a daemon. Type

    # ./acron.py -h

for more information. Do not forget to start your jobs.
