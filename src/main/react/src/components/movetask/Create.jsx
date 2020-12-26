import {Button, Checkbox, Form, message, Select, Steps} from 'antd';
import Nav from '../common/Nav';
import React from 'react';

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

const steps = [
  {
    title: '选择起始数据源',
    content: (<Form
      {...layout}

      name="basic"


    >

      <Form.Item
        label="数据源"
        name="dtsName"
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
      <Form.Item name="dtsType" label="Bucket" rules={[{ required: true, message: '请选择Bucket' }]}>
        <Select
          placeholder="请选择Bucket"
          allowClear
        >
        </Select>
      </Form.Item>

      <Form.Item label="全量迁移">

        <Checkbox></Checkbox>



      </Form.Item>




    </Form>),
  },
  {
    title: '选择目的数据源',
    content: 'Second-content',
  },
  {
    title: '参数设置',
    content: <div>123</div>,
  },
];

const Create = () => {
  const [current, setCurrent] = React.useState(0);

  const next = () => {
    setCurrent(current + 1);
  };

  const prev = () => {
    setCurrent(current - 1);
  };


  return <div className="content-wrapper">

    <Nav />
    <div className="content-body">

      <div className="content" >
        <Steps progressDot current={current}>
          {steps.map(item => (
            <Step key={item.title} title={item.title} />
          ))}
        </Steps>
        <div className="steps-content">{steps[current].content}
          <div className="steps-action" style={{ marginLeft: '17%' }}>
            {current < steps.length - 1 && (
              <Button type="primary" onClick={() => next()}>
                下一步
              </Button>
            )}
            {current === steps.length - 1 && (
              <Button type="primary" onClick={() => message.success('Processing complete!')}>
                确定
              </Button>
            )}
            {current > 0 && (
              <Button style={{ margin: '0 8px' }} onClick={() => prev()}>
                上一步
              </Button>
            )}
          </div>
        </div>


      </div>
    </div>
  </div>
}


export default Create;