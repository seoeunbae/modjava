# Output of modernized codes

## Migreate Java to Spring Boot

```bash
mvn -f mod2.out/pom.xml install

sudo dnf install docker
$ systemctl --user enable --now podman.socket
Created symlink /home/ducdo/.config/systemd/user/sockets.target.wants/podman.socket → /usr/lib/systemd/user/podman.socket.

# Java 11

sudo dnf install java-11-openjdk
sudo dnf install java-11-openjdk-devel
sudo dnf install tzdata-java
sudo alternatives --config java

# for spring boot 3
sudo dnf install java-17-openjdk
sudo dnf install java-17-openjdk-devel
sudo alternatives --config java
sudo alternatives --config javac
# Tell the agent to use podman instead of the buggy docker.service

# chrome browser for gui tests
sudo sh -c 'echo "[google-chrome]" > /etc/yum.repos.d/google-chrome.repo && echo "name=google-chrome" >> /etc/yum.repos.d/google-chrome.repo && echo "baseurl=http://dl.google.com/linux/chrome/rpm/stable/x86_64" >> /etc/yum.repos.d/google-chrome.repo && echo "enabled=1" >> /etc/yum.repos.d/google-chrome.repo && echo "gpgcheck=1" >> /etc/yum.repos.d/google-chrome.repo && echo "gpgkey=https://dl.google.com/linux/linux_signing_key.pub" >> /etc/yum.repos.d/google-chrome.repo'
sudo dnf install google-chrome-stable -y
```

Error logs

```logs
I'm currently in Phase 4: End-to-End Verification with Integration Tests. I'm still working on setting up the integration
  test module, but I'm blocked by the issue with the chromedriver.

DOCKER_HOST=unix:///run/user/1001/podman/podman.sock JAVA_HOME=/usr/lib/jvm/java-11-openjdk mvn -f mod2.out/web… │

The integration tests failed because the PostgreSQL container ran out of disk space during startup. The error
  message "FATAL: could not write to file \"pg_wal/xlogtemp.38\": No space left on device" indicates this.
  $ df -h /
Filesystem      Size  Used Avail Use% Mounted on
/dev/nvme0n1p2   20G   20G   20K 100% /

--> extended the disk space to 500G
```