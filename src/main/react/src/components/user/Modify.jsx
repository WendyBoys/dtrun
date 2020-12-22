import React from 'react';
import Nav from '../common/Nav';
import {Form, Input, Button, message} from 'antd';
import axios from "axios/index";
import {notification} from "antd/lib/index";


const layout = {
    labelCol: {
        span: 3,
    },
    wrapperCol: {
        span: 16,
    },
};

const tailLayout = {
    wrapperCol: {
        offset: 3,
        span: 16,
    },
};


export default class Modfiy extends React.Component {

    onFinish = (values) => {
        axios.post('http://localhost/user/modifyPassword', {
            oldPassword: values.oldPassword,
            newPassword: values.newPassword
        }).then((response) => {
            var result = response.data.message;
            if (result == 'Success') {
                this.props.history.push('/index');
                notification['success']({
                    message: '通知',
                    description:
                        '修改成功',
                    duration: 1,
                });
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '旧密码错误,请重新输入',
                    duration: 1,
                });
            }
        });
    }

    quit() {
        this.props.history.goBack();
    }


    render() {
        return (
            <div className="content-wrapper">

                <Nav/>
                <div className="content-body">

                    <div id="createdts" className="content">
                        <div className="page-header d-flex justify-content-between">
                            <h2>修改密码</h2>
                        </div>
                        <Form
                            {...layout}
                            ref={this.formRef}
                            name="basic"
                            initialValues={{
                                remember: true,
                            }}
                            onFinish={this.onFinish}
                        >
                            <Form.Item
                                label="旧密码"
                                name="oldPassword"
                                rules={[
                                    {
                                        required: true,
                                        message: '请输旧密码',
                                    },
                                ]}
                            >

                                <Input type='text'
                                       placeholder="请输入旧密码"/>
                            </Form.Item>
                            <Form.Item
                                label="新密码"
                                name="newPassword"
                                rules={[
                                    {
                                        compare: '11111',
                                        required: true,
                                        message: '请输新密码',
                                    },
                                ]}
                            >

                                <input placeholder="请输新密码" type="text" id="basic_dtsName"
                                       class="ant-input" value=""/>
                            </Form.Item>

                            <Form.Item {...tailLayout}>
                                <Button type="primary" htmlType="submit" style={{marginRight: '10px'}}
                                >
                                    确定
                                </Button>

                                <Button htmlType="button" onClick={() => this.quit()}>
                                    取消
                                </Button>
                            </Form.Item>
                        </Form>

                    </div>

                </div>

            </div>

        )

    }
}