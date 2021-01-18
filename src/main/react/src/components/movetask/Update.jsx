import {Button, Checkbox, Divider, Form, Input, notification, Popover, Select, Spin, Steps} from 'antd';
import React, {useEffect, useState} from 'react';
import {createBucket, getAllDtSourceName, getBucketLists} from '../datasource/service';
import {getMoveTaskById, updateMoveTask} from './service';
import {PlusOutlined} from '@ant-design/icons';

const {Step} = Steps;
const {Option} = Select;
const layout = {
    labelCol: {
        span: 4,
    },
    wrapperCol: {
        span: 16,
    },
};


const Update = (props) => {
    const [loading, setLoading] = useState(false);
    const [current, setCurrent] = useState(0);
    const [data, setData] = useState({});
    const [checked, setChecked] = useState(true);
    const [dtsList, setDtsList] = useState([]);
    const [srcBucketList, setSrcBucketList] = useState([]);
    const [desBucketList, setDesBucketList] = useState([]);
    const [showAddBucket, setShowAddBucket] = useState(false);
    const [newBucketName, setNewBucketName] = useState([]);

    const [form] = Form.useForm();
    useEffect(() => {
        setLoading(true)
        getAllDtSourceName().then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setDtsList(response.data.data)
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '获取数据源列表失败，请检查数据源配置',
                    duration: 2,
                });
            }
            setLoading(false)
        });
        getMoveTaskById({
            id: props.match.params.id
        }).then((response) => {
            const result = response.data.message;
            const data = response.data.data;
            if (result === 'Success') {
                const taskJson = JSON.parse(data.taskJson);
                form.setFieldsValue({srcBucket: taskJson.srcBucket})
                form.setFieldsValue({desBucket: taskJson.desBucket})
                setChecked(taskJson.allMove !== 'false')
                form.setFieldsValue({srcId: parseInt(taskJson.srcId)})
                form.setFieldsValue({desId: parseInt(taskJson.desId)})
                form.setFieldsValue({taskName: data.taskName})
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '获取迁移任务失败，请检查配置',
                    duration: 2,
                });
            }
            setLoading(false)
        });
    }, []);

    const prev = () => {
        setCurrent(current - 1);
    };

    const onChange = () => {
        setChecked(!checked)
    }

    const srcChange = (id) => {
        form.setFieldsValue({srcBucket: undefined})
        setSrcBucketList([])
        getBucketLists({
            id: id,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setSrcBucketList(response.data.data)
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '获取Bucket列表失败，请检查数据源配置',
                    duration: 2,
                });
            }

        });
    }

    const desChange = (id) => {
        form.setFieldsValue({desBucket: undefined})
        setDesBucketList([])
        getBucketLists({
            id: id
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setDesBucketList(response.data.data)
                setShowAddBucket(true)
            } else {
                setShowAddBucket(false)
                notification['error']({
                    message: '通知',
                    description:
                        '获取Bucket列表失败，请检查数据源配置',
                    duration: 2,
                });
            }
        });
    }

    const addItem = () => {
        const desId = form.getFieldValue('des');
        //发请求创建新的Bucket
        createBucket({
            desId: desId,
            newBucketName: newBucketName,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setNewBucketName([])
                notification['success']({
                    message: '通知',
                    description:
                        '创建Bucket成功',
                    duration: 2,
                });
                // setDesBucketList(response.data.data)
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '创建Bucket列表失败，请检查配置',
                    duration: 2,
                });
            }
        });
    }

    const quit = () => {
        props.history.goBack();

    }

    const onNameChange = event => {
        setNewBucketName(event.target.value)
    }

    const srcOnFinish = values => {
        setData({...data, 'src': values, 'allMove': checked})
        setCurrent(current + 1);
    };

    const desOnFinish = values => {
        setData({...data, 'des': values})
        setCurrent(current + 1);
    };

    const OnFinish = values => {
        const body = {...data, 'option': values}
        updateMoveTask({
            id: props.match.params.id,
            srcId: body.src.srcId,
            srcBucket: body.src.srcBucket,
            desId: body.des.desId,
            desBucket: body.des.desBucket,
            allMove: body.allMove,
            taskName: body.option.taskName,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                notification['success']({
                    message: '通知',
                    description:
                        '修改迁移任务成功',
                    duration: 2,
                });
                props.history.push('/movetask/show');
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '修改迁移任务失败，请检查配置',
                    duration: 2,
                });
            }
        });
    };


    const steps = [
        {
            title: '选择起始数据源',
            content: (<Form
                {...layout}
                name="basic"
                form={form}
                onFinish={srcOnFinish}
            >
                <Form.Item
                    label="起始"
                    name="srcId"
                    rules={[{required: true, message: '请选择数据源'}]}
                >
                    <Select
                        placeholder="请选择数据源"
                        allowClear
                        showSearch
                        optionFilterProp="children"
                        filterOption={(input, option) =>
                            option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                        filterSort={(optionA, optionB) =>
                            optionA.children.toLowerCase().localeCompare(optionB.children.toLowerCase())
                        }
                        onChange={srcChange}
                    >
                        {
                            dtsList.map((item, index) =>
                                <Option key={index} value={item.id}>{item.dtSourceName}</Option>
                            )
                        }
                    </Select>
                </Form.Item>
                <Form.Item
                    name="srcBucket"
                    label="Bucket"
                    rules={[{required: true, message: '请选择Bucket'}]}>
                    <Select
                        placeholder="请选择Bucket"
                        allowClear
                        showSearch
                        optionFilterProp="children"
                        filterOption={(input, option) =>
                            option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                        filterSort={(optionA, optionB) =>
                            optionA.children.toLowerCase().localeCompare(optionB.children.toLowerCase())
                        }
                    >
                        {
                            srcBucketList.map((item, index) =>
                                <Option key={index} value={item}>{item}</Option>
                            )
                        }
                    </Select>
                </Form.Item>

                <Form.Item label="全量迁移">
                    <Checkbox onChange={onChange} checked={checked}></Checkbox>
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit" style={{margin: '0 10px 0 25%'}}>
                        下一步
                    </Button>
                    <Button htmlType="button" onClick={() => quit()}>
                        取消
                    </Button>
                </Form.Item>
            </Form>),
        },
        {
            title: '选择目的数据源',
            content: (<Form
                {...layout}
                name="basic"
                form={form}
                onFinish={desOnFinish}
            >
                <Form.Item
                    label="目的数据源"
                    name="desId"
                    rules={[{required: true, message: '请选择数据源'}]}
                >
                    <Select
                        placeholder="请选择数据源"
                        allowClear
                        showSearch
                        optionFilterProp="children"
                        filterOption={(input, option) =>
                            option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                        filterSort={(optionA, optionB) =>
                            optionA.children.toLowerCase().localeCompare(optionB.children.toLowerCase())
                        }
                        onChange={desChange}
                    >
                        {
                            dtsList.map((item, index) =>
                                <Option key={index} value={item.id}>{item.dtSourceName}</Option>
                            )
                        }
                    </Select>
                </Form.Item>

                <Form.Item
                    name="desBucket"
                    label="Bucket"
                    rules={[{required: true, message: '请选择Bucket'}]}
                >
                    <Select
                        placeholder="请选择Bucket"
                        allowClear
                        showSearch
                        optionFilterProp="children"
                        filterOption={(input, option) =>
                            option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                        filterSort={(optionA, optionB) =>
                            optionA.children.toLowerCase().localeCompare(optionB.children.toLowerCase())
                        }
                        dropdownRender={menu => (
                            <div>
                                {menu}
                                {showAddBucket && <div>
                                    <Divider style={{margin: '4px 0'}}/>
                                    <div style={{display: 'flex', flexWrap: 'nowrap', padding: 8}}>
                                        <Input style={{flex: 'auto'}} value={newBucketName} onChange={onNameChange}/>
                                        <a
                                            style={{flex: 'none', padding: '8px', display: 'block', cursor: 'pointer'}}
                                            onClick={addItem}
                                        >
                                            <PlusOutlined/> 创建Bucket
                                        </a>
                                    </div>
                                </div>}
                            </div>
                        )}
                    >
                        {
                            desBucketList.map((item, index) =>
                                <Option key={index} value={item}>{item}</Option>
                            )
                        }
                    </Select>
                </Form.Item>


                <Form.Item>
                    <Button type="primary" htmlType="submit" style={{margin: '0 10px 0 25%'}}>
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
                    name="taskName"
                    rules={[{required: true, message: '请选择Bucket'}]}
                >
                    <Input placeholder="请输入任务名称"/>
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" style={{margin: '0 10px 0 25%'}}>
                        确定
                    </Button>
                    <Button type="primary" htmlType="button" onClick={prev}>
                        上一步
                    </Button>
                </Form.Item>
            </Form>),
        },
    ];


    const customDot = (dot, {status, index}) => (
        <Popover
            content={
                <span>
              step {index}
            </span>
            }
        >
            {dot}
        </Popover>
    );


    return <Spin size="large" spinning={loading}>
        <Steps forcerender="true" progressDot={customDot} current={current}>
            {steps.map(item => (
                <Step key={item.title} title={item.title}/>
            ))}
        </Steps>
        <div className="steps-content">{steps[current].content}
            <div className="steps-action">
            </div>
        </div>
    </Spin>
}


export default Update;