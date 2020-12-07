import React from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import axios from 'axios';
import cookie from 'react-cookies'
import { withRouter } from 'react-router-dom';
class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            title: '登录',
        }
    }



    login() {

        axios.post('http://localhost/user/login', {
            account: this.account.value,
            password: this.password.value,
        }).then((response) => {
            var result = response.data.message;
            var token = response.data.data;
            if (result == 'Success') {
                let inFifteenMinutes = new Date(new Date().getTime() + 7 * 24 * 3600 * 1000);
                cookie.save('token', token, { expires: inFifteenMinutes })
                this.props.history.push('/index')
            } else if (result == 'LoginRefuse') {
                alert('账号已被封禁')
            } else {
                alert('账号或密码错误')
            }

        });
    }


    register() {
        this.props.history.push('/register')
    }

    render() {
        return <div>
            <div className="modal-dialog" style={{ marginTop: '10%' }}>
                <div className="modal-content">
                    <div className="modal-header">

                        <h4 className="modal-title text-center" >{this.state.title}</h4>
                    </div>
                    <div className="modal-body" >
                        <div className="form-group">

                            <input type="text" className="form-control" placeholder="账号" ref={input => this.account = input} autoComplete="off" />
                        </div>
                        <div className="form-group">

                            <input type="password" className="form-control" placeholder="密码" ref={input => this.password = input} autoComplete="off" />
                        </div>
                    </div>
                    <div className="modal-footer">
                        <div className="form-group">
                            <button type="button" style={{ outline: 'none' }} className="btn btn-primary form-control" onClick={() => this.login()}>登录</button>
                        </div>
                        <div className="form-group">
                            <button type="button" style={{ outline: 'none' }} className="btn btn-success form-control" onClick={() => this.register()}>注册</button>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    }
}

export default withRouter(Login);