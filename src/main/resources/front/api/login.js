function loginApi(data) {
    return $axios({
      'url': '/user/login',
      'method': 'post',
      data
    })
  }

function sendMsgApi(data) {//发送获取验证码的请求
    return $axios({
        'url': '/user/sendMsg',
        'method': 'post',
        data
    })
}


function loginoutApi() {
  return $axios({
    'url': '/user/loginout',
    'method': 'post',
  })
}

  