.PHONY: default test help

default: help

PROJECT_VERSION := $(shell mvn help:evaluate -q -DforceStdout -D"expression=project.version")
PROJECT_NAME=$(shell mvn help:evaluate -q -DforceStdout -D"expression=project.name")
PROJECT_DOCKER_REPOSITORY=alphnology/${PROJECT_NAME}
LATEST=latest

COMMIT := $(shell git rev-parse --short HEAD)

# COLORS
GREEN  := $(shell tput -Txterm setaf 2)
YELLOW := $(shell tput -Txterm setaf 3)
WHITE  := $(shell tput -Txterm setaf 7)
RESET  := $(shell tput -Txterm sgr0)


TARGET_MAX_CHAR_NUM=20
# Show this help.
help:
	@echo ''
	@echo '       ${YELLOW}Project ${GREEN}${PROJECT_NAME}${RESET}'
	@echo ''
	@echo 'Usage:'
	@echo '  ${YELLOW}make${RESET} ${GREEN}<target>${RESET}'
	@echo ''
	@echo 'Targets:'
	@awk '/^[a-zA-Z\-\_0-9]+:/ { \
		helpMessage = match(lastLine, /^## (.*)/); \
		if (helpMessage) { \
			helpCommand = substr($$1, 0, index($$1, ":")-1); \
			helpMessage = substr(lastLine, RSTART + 3, RLENGTH); \
			printf "  ${YELLOW}%-$(TARGET_MAX_CHAR_NUM)s${RESET} ${GREEN}%s${RESET}\n", helpCommand, helpMessage; \
		} \
	} \
	{ lastLine = $$0 }' $(MAKEFILE_LIST)

## Clean docker image
clean:
	@echo '${GREEN}Clean docker image ${RESET}'$(PROJECT_NAME)
	docker-compose down -v
	docker-compose up -d

restart: clean
	@echo '${GREEN}Restart docker image ${RESET}'$(PROJECT_NAME)
ifeq ($(OS),Windows_NT)
	@.\mvnw.cmd compile quarkus:dev
else
	@./mvnw compile quarkus:dev
endif



