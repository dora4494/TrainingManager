
function init_playwright {
  pushd playwright_test
  npm install
  popd
}

function launch_test {
  echo "Launch application"
  docker compose --profile test up -d --force-recreate
}

function wait_result {
  echo "Waiting for the end of playwright tests..."

  while [ -n "$(docker compose ps -q playwright)" ]
  do
    sleep 2
    echo "test..."
  done
  echo "End of playwright tests"
}

function clean_environment {
  echo "Stop docker"
  docker compose down
}

function display_result {
  echo "Display results"
  xdg-open playwright_test/playwright-report/index.html
}

init_playwright
launch_test
wait_result
clean_environment
# display_result