import React from 'react';
import Nav from '../common/Nav';
import axios from 'axios';
import {Form, Input, Button, Select} from 'antd';
import {notification} from 'antd';

const {Option} = Select;

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
export default class Update extends React.Component {
    formRef = React.createRef();

    constructor(props) {
        super(props);
        this.state = {}


    }


    componentWillMount() {

        this.getDts()

    }


    getDts() {
        axios.get('/dtsource/getDtSourceById?id=' + this.props.match.params.id).then((response) => {
            const result = response.data.message;
            if (result == 'Success') {
                const data = response.data.data;
                const dtSourceJson = JSON.parse(data.dtSourceJson);
                this.formRef.current.setFieldsValue(
                    {
                        dtsName: data.dtSourceName,
                        dtsType: data.dtSourceType,
                        accessKey: dtSourceJson.accessKey,
                        accessSecret: dtSourceJson.accessSecret,
                        region: dtSourceJson.region
                    },
                );
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '获取数据源信息失败，请稍后重试',
                    duration: 1,
                });
            }
        });
    }

    test() {
        this.formRef.current.validateFields()
            .then(values => {
                axios.post('/dtsource/testconnection', {
                    dataSourceType: values.dtsType,
                    accessKey: values.accessKey,
                    accessSecret: values.accessSecret,
                    region: values.region,
                }).then((response) => {
                    var result = response.data.message;
                    if (result == 'Success') {
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
                                '测试失败，请检查参数',
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
        axios.post('/dtsource/update', {
            id: this.props.match.params.id,
            dataSourceName: values.dtsName,
            dataSourceType: values.dtsType,
            secretId: values.accessKey,
            secretKey: values.accessSecret,
            region: values.region,
        }).then((response) => {
            var result = response.data.message;
            if (result == 'Success') {
                notification['success']({
                    message: '通知',
                    description:
                        '修改成功',
                    duration: 1,
                });
                this.props.history.push('/datasource/show');
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '修改失败,请重试',
                    duration: 1,
                });

            }
        });
    };


    render() {
        return <div className="content-wrapper">
            <Nav/>
            <div className="content-body">

                <div id="createdts" className="content">
                    <div className="page-header d-flex justify-content-between">
                        <h2>修改数据源</h2>

                    </div>

                    <div className="row">

                        <div className="col-xl-12 components-sidebar">

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

                                    rules={[
                                        {
                                            required: true,
                                            message: '请输入数据源名称',
                                        },
                                    ]}
                                >
                                    <Input placeholder="请输入数据源名称"/>
                                </Form.Item>
                                <Form.Item name="dtsType" label="数据源类型" rules={[{required: true, message: '请选择数据源类型'}]}>
                                    <Select
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
                                    label="AccessKey"
                                    name="accessKey"
                                    rules={[
                                        {
                                            required: true,
                                            message: '请输入AccessKey',
                                        },
                                    ]}
                                >
                                    <Input placeholder="请输入AccessKey"/>
                                </Form.Item>

                                <Form.Item
                                    label="AccessSecret"
                                    name="accessSecret"
                                    rules={[
                                        {
                                            required: true,
                                            message: '请输入AccessSecret',
                                        },
                                    ]}
                                >
                                    <Input placeholder="请输入AccessSecret"/>
                                </Form.Item>


                                <Form.Item
                                    label="地域"
                                    name="region"
                                    rules={[
                                        {

                                            message: '请输入地域',
                                        },
                                    ]}
                                >
                                    <Input placeholder="请输入地域"/>
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
                        </div>

                    </div>
                </div>


            </div>
        </div>


    }
}