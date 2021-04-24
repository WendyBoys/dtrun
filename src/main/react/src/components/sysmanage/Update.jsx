import React from 'react';
import { updateWhiteList,getWhiteListById} from '../sysmanage/service'
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
        getWhiteListById({
            id: this.props.match.params.id,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                const data = response.data.data;
                this.formRef.current.setFieldsValue(
                    {
                        ip: data.ip,
                    },
                );
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '获取信息失败，请稍后重试',
                    duration: 1,
                });
            }
        });
    }

    quit() {
        this.props.history.goBack();
    }


    onFinish = (values) => {
        updateWhiteList({
            id: this.props.match.params.id,
            ip: values.ip,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                this.props.history.push('/sysmanage/whitelist');
            } else {

            }
        });
    };

    render() {
        return <div style={{padding: '10px'}}>
            <h2>修改白名单</h2>
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
                            label="ip地址"
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
