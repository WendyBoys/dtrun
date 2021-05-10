import React from 'react';
import {updateContact, getContactById} from '../pushconfig/service'
import {Button, Col, Form, Input, notification, Row, Select} from 'antd';

const {Option} = Select;

const layout = {
    labelCol: {
        span: 3,
    },
    wrapperCol: {
        span: 12,
    },
};
const tailLayout = {
    wrapperCol: {
        offset: 3,
        span: 16,
    },
};
export default class Pushconfig extends React.Component {
    formRef = React.createRef();

    constructor(props) {
        super(props);
    }

    componentWillMount() {
        getContactById({
            id: this.props.match.params.id,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                const data = response.data.data;
                this.formRef.current.setFieldsValue(
                    {
                        contactName: data.contactName,
                        contactEmail: data.contactEmail,
                    },
                );
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '获取联系人信息失败，请稍后重试',
                    duration: 1,
                });
            }
        });
    }

    quit() {
        this.props.history.goBack();
    }


    onFinish = (values) => {
        updateContact({
            id: this.props.match.params.id,
            contactName: values.contactName,
            contactEmail: values.contactEmail,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                this.props.history.push('/pushconfig/contact');
            } else if(result === 'CreateRepeat') {
                notification['error']({
                    message: '通知',
                    description:
                        '联系人名称已存在，请重新输入',
                    duration: 1,
                });
            }
        });
    };
    render() {
        return <div style={{padding: '10px'}}>
            <h2>修改联系人</h2>
            <Row>
                <Col span={20}>
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
                            label="联系人名称"
                            name="contactName"
                            rules={[
                                {
                                    required: true,
                                    message: '请输入联系人名称',
                                },
                            ]}
                        >
                            <Input placeholder="请输入联系人名称"/>
                        </Form.Item>

                        <Form.Item
                            name="contactEmail"
                            label="联系人邮箱"
                            rules={[
                                {
                                    type: 'email',
                                    message: '请输入正确格式的邮箱地址',
                                },
                                {
                                    required: true,
                                    message: '请输入联系人邮箱',
                                },
                            ]}
                        >
                            <Input placeholder="请输入联系人邮箱"/>
                        </Form.Item>
                        <Form.Item {...tailLayout}>
                            <Button type="primary" htmlType="submit" style={{marginRight: '10px'}}>
                                确定
                            </Button>
                            <Button htmlType="button" onClick={() => this.quit()}>
                                取消
                            </Button>
                        </Form.Item>
                    </Form>
                </Col>
            </Row>
        </div>

    }
}
