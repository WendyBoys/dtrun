import React from 'react';
import {Button, Col, Form, Input, notification, Row, Select} from 'antd';
import {createWhiteList} from "../sysmanage/service";


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

export default class Whitelist extends React.Component {
    formRef = React.createRef();
    constructor(props) {
        super(props);
    }
    quit() {
        this.props.history.goBack();
    }
    onFinish = (values) => {
        createWhiteList({
            ip: values.ip,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                this.props.history.push('/sysmanage/whitelist');
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '创建失败，ip地址已存在,此ip可以登录',
                    duration: 1,
                });
            }
        });
    };

    render() {
        return <div style={{padding: '10px'}}>
            <h2>创建白名单</h2>
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
                               label="IP地址"
                               name="ip"
                               rules={[
                                   {
                                       required: true,
                                       message: '请输入ip地址',
                                   },
                               ]}
                           >
                               <Input placeholder="请输入ip地址"/>
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
