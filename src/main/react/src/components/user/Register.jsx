import React from 'react';
import '../../css/css2.css';
import socket from '../../images/socket.svg';
import axios from 'axios';
import {Form, Input, Button, Checkbox} from 'antd';
import {UserOutlined, LockOutlined} from '@ant-design/icons';
import {NavLink} from "react-router-dom";


export default class Register extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            title: '注册',
        }
    }


    onFinish = (values) => {
        axios.post('http://localhost/user/register', {
            account: values.account,
            password: values.password,
        }).then((response) => {
            const result = response.data.message;
            if (result == 'Success') {
                this.props.history.push('/login')
            } else {
                alert('注册失败');
            }
        });

    };

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
                        </div>
                    </div>
                    <div className="col-xl-3"></div>
                </div>


            </div>);


    }
}


