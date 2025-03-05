# Use a Debian-based JDK image instead of Alpine
FROM eclipse-temurin:21-jdk

# Set metadata
LABEL authors="hilda"

WORKDIR /app

RUN apt-get update && apt-get install -y maven wget unzip \
    && wget https://download2.gluonhq.com/openjfx/21/openjfx-21_linux-x64_bin-sdk.zip \
    && unzip openjfx-21_linux-x64_bin-sdk.zip -d /usr/share/ \
    && rm openjfx-21_linux-x64_bin-sdk.zip

COPY . /app/

# Build the application inside the container
RUN mvn clean package

CMD ["java", "--module-path", "/usr/share/javafx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "target/main.jar"]
