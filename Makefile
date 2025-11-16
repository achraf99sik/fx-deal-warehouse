.PHONY: all build test docker-build docker-up run clean

APP_NAME = fx-deal-warehouse
APP_VERSION = 1.0-SNAPSHOT
JAR_FILE = target/$(APP_NAME)-$(APP_VERSION).jar
DOCKER_IMAGE_NAME = $(APP_NAME):$(APP_VERSION)

all: build docker-build

build:
	@echo "Building Maven project..."
	@mvn clean install

test:
	@echo "Running unit tests..."
	@mvn test

docker-up:
	@echo "Starting PostgreSQL database with Docker Compose..."
	@docker-compose up -d db
	@echo "Waiting for database to be ready..."
	@until docker-compose exec db pg_isready -U user -d fx_deal_db; do \
		printf "."; \
		sleep 1; \
	done;
	@echo "Database is ready!"


docker-build: build
	@echo "Building Docker image for the application..."
	@docker build -t $(DOCKER_IMAGE_NAME) .

run: docker-up docker-build
	@echo "Running the application in a Docker container..."
	@docker run --rm --network progresssoft_default $(DOCKER_IMAGE_NAME)

clean:
	@echo "Cleaning up..."
	@mvn clean
	@docker-compose down -v
	@docker rmi $(DOCKER_IMAGE_NAME) || true
	@rm -f $(JAR_FILE)
