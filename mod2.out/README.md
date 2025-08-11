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
sudo dnf install java-11-openjdk.x86_64
sudo dnf install tzdata-java
sudo alternatives --config java

# Tell the agent to use podman instead of the buggy docker.service
```

Error logs

```logs
I'm currently in Phase 4: End-to-End Verification with Integration Tests. I'm still working on setting up the integration
  test module, but I'm blocked by the issue with the chromedriver.

DOCKER_HOST=unix:///run/user/1001/podman/podman.sock JAVA_HOME=/usr/lib/jvm/java-11-openjdk mvn -f mod2.out/web… │
```