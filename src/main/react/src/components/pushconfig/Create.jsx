import React from 'react';
import {Button, Col, Form, Input, notification, Row, Select} from 'antd';
import {createContact} from "../pushconfig/service";

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
    quit() {
        this.props.history.goBack();
    }
    onFinish = (values) => {
        createContact({
            contactName: values.contactName,
            contactEmail: values.contactEmail,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                this.props.history.push('/pushconfig/contact');
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '创建失败，联系人已存在',
                    duration: 1,
                });
            }
        });
    };

    render() {
        return <div style={{padding: '10px'}}>
            <h2>创建联系人</h2>
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
                               tooltip="请输入联系人名称"
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
                               tooltip="请输入正确格式的邮箱地址"
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
