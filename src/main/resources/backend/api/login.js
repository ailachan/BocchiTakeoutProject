function loginApi(data) {
  return $axios({
    'url': '/employee/login',
    'method': 'post',
    data//携带json参数,username,password
  })
}

function logoutApi(){
  return $axios({
    'url': '/employee/logout',
    'method': 'post',
  })
}
