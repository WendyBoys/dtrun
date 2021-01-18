import React from 'react';
import socket from '../../images/socket.svg';
import {register} from './service'
import {Button, Col, Form, Input, notification, Row} from 'antd';
import {AuditOutlined, LockOutlined, UserOutlined} from '@ant-design/icons';
import {NavLink} from "react-router-dom";


export default class Register extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            title: '注册',
        }
    }

    onFinish = (values) => {
        register({
            account: values.account,
            password: values.password,
            registerCode: values.registerCode,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                this.props.history.push('/login')
            }
            notification[result === 'Success' ? 'success' : 'error']({
                message: '通知',
                description:
                response.data.data,
                duration: 2,
            });
        });

    };

    render() {
        return (
            <div>
                <Row style={{marginBottom: '5%', paddingTop: '20px', minHeight: '200px'}}>
                    <Col span={6}></Col>
                    <Col span={12}><img src={socket} alt="" width="100%" height="200px"/></Col>
                    <Col span={6}></Col>
                </Row>
                <Row>
                    <Col span={6}></Col>
                    <Col span={12}>
                        <Form
                            name="normal_login"
                            className="login-form"
                            initialValues={{remember: true}}
                            onFinish={this.onFinish}
                        >
                            <Form.Item
                                name="account"
                                rules={[{required: true, message: '请输入账号'}]}
                            >
                                <Input prefix={<UserOutlined className="site-form-item-icon"/>}
                                       placeholder="账号"/>
                            </Form.Item>
                            <Form.Item
                                name="password"
                                rules={[{required: true, message: '请输入密码'}]}
                            >
                                <Input.Password
                                    prefix={<LockOutlined className="site-form-item-icon"/>}
                                    type="password"
                                    placeholder="密码"
                                />
                            </Form.Item>
                            <Form.Item
                                name="registerCode"
                                rules={[{required: true, message: '请输入注册码'}]}
                            >
                                <Input
                                    prefix={<AuditOutlined/>}
                                    placeholder="注册码"/>
                            </Form.Item>
                            <Form.Item>


                                <NavLink className="login-form-forgot" to="/login">
                                    已有账号？去登录
                                </NavLink>
                            </Form.Item>

                            <Form.Item>
                                <Button type="primary" htmlType="submit" className="login-form-button"
                                        style={{marginBottom: '20px'}}>
                                    立即注册
                                </Button>
                            </Form.Item>
                        </Form>
                    </Col>
                    <Col span={6}></Col>
                </Row>
            </div>);


    }
}


