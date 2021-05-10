import React from 'react';
import {createDataSource, testconnection} from './service';
import {Button, Col, Form, Input, notification, Row, Select,Tooltip,Space,Typography} from 'antd';
import {LockOutlined, UserOutlined,QuestionCircleOutlined} from '@ant-design/icons';
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

const text = <span>prompt text</span>;


export default class Create extends React.Component {
    formRef = React.createRef();

    constructor(props) {
        super(props);
    }

    test() {
        this.formRef.current.validateFields()
            .then(values => {
                testconnection({
                    dataSourceType: values.dtsType,
                    accessKey: values.accessKey,
                    accessSecret: values.accessSecret,
                    region: values.region,
                }).then((response) => {
                    const result = response.data.message;
                    if (result === 'Success') {
                        notification['success']({
                            message: '通知',
                            description:
                                '测试成功',
                            duration: 1,
                        });
                    } else {
                        notification['error']({
                            message: '通知',
                            description:
                                '测试失败,请检查参数',
                            duration: 1,
                        });
                    }
                });
            })
            .catch(errorInfo => {

            });

    }

    quit() {
        this.props.history.goBack();
    }

    onFinish = (values) => {
        createDataSource({
            dataSourceName: values.dtsName,
            dataSourceType: values.dtsType,
            secretId: values.accessKey,
            secretKey: values.accessSecret,
            region: values.region,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                this.props.history.push('/datasource/show');
            } else  if (result ==='CreateRepeat'){
                notification['error']({
                    message: '通知',
                    description:
                        '创建失败，请不要使用已存在的数据源名称',
                    duration: 1,
                });

            }else {
                notification['error']({
                    message: '通知',
                    description:
                        '创建失败，请检查您的参数设置',
                    duration: 1,
                });

            }
        });
    };

    render() {
        return <div style={{padding: '10px'}}>
            <h2>创建数据源</h2>
            <Row>
                <Col span={24}>
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
                                label="数据源名称"
                                name="dtsName"
                                tooltip="请不要使用重复的数据源名称"
                                rules={[
                                    {
                                        required: true,
                                        message: '请输入数据源名称',
                                    },
                                ]}
                            >
                                <Input  style={{ width: 600 }} placeholder="请输入数据源名称"/>
                            </Form.Item>

                        <Form.Item
                            tooltip="COS：腾讯云数据源类型，OSS：阿里云数据源类型，OBS:华为云数据源类型，BOS：百度云数据源类型" name="dtsType" label="数据源类型" rules={[{required: true, message: '请选择数据源类型'}]}>
                            <Select

                                style={{ width: 600 }}
                                placeholder="请选择数据源类型"
                                onChange={this.onGenderChange}
                                allowClear

                            >
                                <Option value="cos">COS</Option>
                                <Option value="oss">OSS</Option>
                                <Option value="obs">OBS</Option>
                                <Option value="bos">BOS</Option>
                            </Select>
                        </Form.Item>

                        <Form.Item
                            tooltip=" 登录云服务网站后在API 密钥中查看,API 密钥是构建云 API 请求的重要凭证，使用 API 可以操作您名下的所有云资源。"
                            label="AccessKey"
                            name="accessKey"
                            rules={[
                                {
                                    required: true,
                                    message: '请输入AccessKey',
                                },
                            ]}
                        >
                            <Input  style={{ width: 600 }}  placeholder="请输入AccessKey"/>

                        </Form.Item>

                        <Form.Item
                            tooltip=" 登录云服务网站后在API 密钥中查看,API 密钥是构建云 API 请求的重要凭证，使用 API 可以操作您名下的所有云资源。"
                            label="AccessSecret"
                            name="accessSecret"
                            rules={[
                                {
                                    required: true,
                                    message: '请输入AccessSecret',
                                },
                            ]}
                        >
                            <Input  style={{ width: 600 }}  placeholder="请输入AccessSecret"/>

                        </Form.Item>

                        <Form.Item
                            tooltip="登录云服务网站后在对象存储中的 Bucket 列表 可查看所有数据源地域"
                            label="地域"
                            name="region"
                            rules={[
                                {

                                    message: '请输入地域',
                                },
                            ]}
                        >
                            <Input  style={{ width: 600 }}  placeholder="请输入地域"/>

                        </Form.Item>

                        <Form.Item {...tailLayout}>
                            <Button type="primary" htmlType="submit" style={{marginRight: '10px'}}>
                                确定
                            </Button>
                            <Button htmlType="button" onClick={() => this.test()} style={{marginRight: '10px'}}>
                                测试
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