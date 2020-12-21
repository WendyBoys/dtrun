import React from 'react';
import '../../css/css2.css';
import socket from '../../images/socket.svg';
import axios from 'axios';
import cookie from 'react-cookies'
import {Button, Checkbox, Form, Input, notification} from 'antd';
import {LockOutlined, UserOutlined} from '@ant-design/icons';

export default class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            title: '登录',
        }
    }


    onFinish = (values) => {
        axios.post('/user/login', {
            account: values.account,
            password: values.password,
        }).then((response) => {
            var result = response.data.message;
            var token = response.data.data;
            if (result == 'Success') {
                let inFifteenMinutes = new Date(new Date().getTime() + 7 * 24 * 3600 * 1000);
                cookie.save('token', token, {expires: inFifteenMinutes})
                this.props.history.push({pathname:'/index'})
            } else if (result == 'LoginRefuse') {
                notification['error']({
                    message: '通知',
                    description:
                        '账号已被封禁',
                    duration: 2,
                });
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '账号或密码错误',
                    duration: 2,
                });
            }

        });

    };

    register() {
        this.props.history.push('/register')
    }

    render() {
        return (

            <div className="container-fluid">
                <div className="row" style={{marginBottom: '5%', paddingTop: '20px', minHeight: '200px'}}>
                    <div className="col-xl-3"></div>
                    <div className="col-xl-6"><img src={socket} alt="" width="100%" height="200px"/></div>
                    <div className="col-xl-3">
                        <div>

                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-xl-3"></div>
                    <div className="col-xl-6">
                        <div>
                            <Form
                                name="normal_login"
                                className="login-form"
                                initialValues={{remember: true}}
                                onFinish={this.onFinish}
                            >
                                <Form.Item
                                    name="account"
                                    rules={[{required: true, message: 'Please input your Username!'}]}
                                >
                                    <Input prefix={<UserOutlined className="site-form-item-icon"/>}
                                           placeholder="Username"/>
                                </Form.Item>
                                <Form.Item
                                    name="password"
                                    rules={[{required: true, message: 'Please input your Password!'}]}
                                >
                                    <Input.Password
                                        prefix={<LockOutlined className="site-form-item-icon"/>}
                                        type="password"
                                        placeholder="Password"
                                    />
                                </Form.Item>
                                <Form.Item>
                                    <Form.Item name="remember" valuePropName="checked" noStyle>
                                        <Checkbox>记住我</Checkbox>
                                    </Form.Item>

                                    <a className="login-form-forgot" href="">
                                        忘记密码
                                    </a>
                                </Form.Item>

                                <Form.Item>
                                    <Button type="primary" htmlType="submit" className="login-form-button"
                                            style={{marginBottom: '20px'}}>
                                        登录
                                    </Button>
                                    <Button type="primary" htmlType="button" className="login-form-button"
                                            onClick={() => this.register()}>
                                        立即注册
                                    </Button>
                                </Form.Item>
                            </Form>
                        </div>
                    </div>
                    <div className="col-xl-3"></div>
                </div>


            </div>);


    }
}


