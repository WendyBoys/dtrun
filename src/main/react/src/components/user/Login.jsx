import React from 'react';
import socket from '../../images/socket.svg';
import cookie from 'react-cookies'
import {Button, Checkbox, Col, Form, Input, notification, Row} from 'antd';
import {LockOutlined, UserOutlined} from '@ant-design/icons';
import {login} from './service'

export default class Login extends React.Component {

    onFinish = (values) => {
        login({
            account: values.account,
            password: values.password,
        }).then(response => {
            const result = response.data.message;
            const token = response.data.data;
            if (result === 'Success') {
                let inFifteenMinutes = new Date(new Date().getTime() + 7 * 24 * 3600 * 1000);
                cookie.save('token', token, {path: '/', expires: inFifteenMinutes})
                this.props.history.push({pathname: '/index'})
            } else if (result === 'LoginRefuse') {
                notification['error']({
                    message: '通知',
                    description:
                        '账号已被封禁',
                    duration: 2,
                });
            } else if (result === 'LoginIpLimit') {
                notification['error']({
                    message: '通知',
                    description:
                        'IP地址不在白名单中，无法登陆',
                    duration: 2,
                });
            }
            else {
                notification['error']({
                    message: '通知',
                    description:
                        '账号或密码错误',
                    duration: 2,
                });
            }
        })
    };

    register() {
        this.props.history.push('/register')
    }

    render() {
        return (
            <div>
                <Row style={{marginBottom: '5%', paddingTop: '20px', minHeight: '200px'}}>
                    <Col span={6}></Col>
                    <Col span={12}><img src={socket} alt="" width="100%" height="200px"/></Col>
                    <Col span={6}>
                        <div>

                        </div>
                    </Col>
                </Row>
                <Row>
                    <Col span={6}></Col>
                    <Col span={12}>
                        <div>
                            <Form
                                name="normal_login"
                                className="login-form"
                                initialValues={{remember: true}}
                                onFinish={this.onFinish}
                            >
                                <Form.Item
                                    name="account"
                                    label="账号"
                                    rules={[{required: true, message: '请输入账号'}]}
                                >
                                    <Input prefix={<UserOutlined className="site-form-item-icon"/>}
                                           placeholder="请输入账号"/>
                                </Form.Item>
                                <Form.Item
                                    name="password"
                                    label="密码"
                                    rules={[{required: true, message: '请输入密码'}]}
                                >
                                    <Input.Password
                                        prefix={<LockOutlined className="site-form-item-icon"/>}
                                        type="password"
                                        placeholder="请输入密码"
                                    />
                                </Form.Item>

                                <Form.Item>
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
                    </Col>
                    <Col span={6}></Col>
                </Row>
                <Row style={{textAlign: 'center'}}>
                    <Col span={6}></Col>
                    <Col span={12} style={{position: 'absolute', left: '45%', bottom: '10px'}}><a
                        href="http://beian.miit.gov.cn">豫ICP备2020036555号</a></Col>
                    <Col span={6}></Col>
                </Row>


            </div>);


    }
}


