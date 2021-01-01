import {Button, Checkbox, Form, Input, Select, Steps, Tooltip} from 'antd';
import React, {useState} from 'react';
import {DiffTwoTone} from '@ant-design/icons';

const { Step } = Steps;
const { Option } = Select;
const layout = {
    labelCol: {
        span: 4,
    },
    wrapperCol: {
        span: 16,
    },
};


const Create = () => {
    const [current, setCurrent] = useState(0);
    const [data, setData] = useState({});
    const [checked, setChecked] = useState(true);

    const prev = () => {
        setCurrent(current - 1);
    };

    const onChange = () => {
        setChecked(!checked)
    }

    const srcOnFinish = values => {
        setData({ ...data, 'src': values, 'allmove': checked })
        setCurrent(current + 1);
    };

    const desOnFinish = values => {
        setData({ ...data, 'des': values })
        setCurrent(current + 1);
    };

    const OnFinish = values => {
        const body = { ...data, 'option': values }
        console.log(body)
    };


    const steps = [
        {
            title: '选择起始数据源',
            content: (<Form
                {...layout}
                name="basic"
                onFinish={srcOnFinish}
            >
                <Form.Item
                    label="数据源"
                    name="src"
                    rules={[{ required: true, message: '请选择数据源' }]}
                >
                    <Select
                        placeholder="请选择数据源"
                        allowClear
                    >
                        <Option value="cos">COS</Option>
                        <Option value="oss">OSS</Option>
                        <Option value="obs">OBS</Option>
                        <Option value="bos">BOS</Option>
                    </Select>
                </Form.Item>
                <Form.Item name="dtsType" label="Bucket">
                    <Select
                        placeholder="请选择Bucket"
                        allowClear
                    >
                        <Option value="a">a</Option>
                        <Option value="b">b</Option>
                        <Option value="c">c</Option>
                        <Option value="d">d</Option>
                    </Select>
                </Form.Item>

                <Form.Item label="全量迁移">
                    <Checkbox onChange={onChange} checked={checked}></Checkbox>
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit" style={{ marginLeft: '25%' }}>
                        下一步
                    </Button>
                </Form.Item>
            </Form>),
        },
        {
            title: '选择目的数据源',
            content: (<Form
                {...layout}
                name="basic"
                onFinish={desOnFinish}
            >
                <Form.Item
                    label="数据源"
                    name="des"
                    rules={[{ required: true, message: '请选择数据源' }]}
                >
                    <Select
                        placeholder="请选择数据源"
                        allowClear
                    >
                        <Option value="cos">COS</Option>
                        <Option value="oss">OSS</Option>
                        <Option value="obs">OBS</Option>
                        <Option value="bos">BOS</Option>
                    </Select>
                </Form.Item>
                <Form.Item name="dtsType" label="Bucket">
                    <Input.Group compact>
                        <Select
                            placeholder="请选择Bucket"
                            allowClear
                            style={{ width: '95%', marginTop: '2px' }}
                        >
                        </Select>
                        <Tooltip title="创建Bucket">
                            <Button type="text" style={{ marginLeft: '1px', width: '5%', background: 'white' }}
                                icon={<DiffTwoTone style={{ fontSize: '25px' }} />}></Button>
                        </Tooltip>
                    </Input.Group>
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit" style={{ margin: '0 10px 0 25%' }}>
                        下一步
                    </Button>
                    <Button type="primary" htmlType="button" onClick={prev}>
                        上一步
                    </Button>
                </Form.Item>
            </Form>),
        },
        {
            title: '参数设置',
            content: (<Form
                {...layout}
                name="basic"
                onFinish={OnFinish}
            >
                <Form.Item
                    label="任务名称"
                    name="taskname"
                    rules={[{ required: true, message: '请选择Bucket' }]}
                >
                    <Input placeholder="请输入任务名称" />
                </Form.Item>
                <Form.Item name="dtsType" label="Bucket">
                    <Select
                        placeholder="请选择Bucket"
                        allowClear
                        style={{ width: '95%', marginTop: '2px' }}
                    >
                    </Select>
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" style={{ margin: '0 10px 0 25%' }}>
                        确定
                    </Button>
                    <Button type="primary" htmlType="button" onClick={prev}>
                        上一步
                    </Button>
                </Form.Item>
            </Form>),
        },
    ];


    return <div >
        <Steps progressDot current={current}>
            {steps.map(item => (
                <Step key={item.title} title={item.title} />
            ))}
        </Steps>
        <div className="steps-content">{steps[current].content}
            <div className="steps-action" style={{ marginLeft: '17%' }}>
            </div>
        </div>
    </div>
}


export default Create;