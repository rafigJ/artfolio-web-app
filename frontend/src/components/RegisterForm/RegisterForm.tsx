import { EnvironmentOutlined, LockOutlined, MailOutlined, PlusOutlined, UploadOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Form, Input, Steps, Typography, Upload, message } from 'antd';
import React, { useState } from 'react';
import '../LoginForm/LoginFormStyles.css'
import { useNavigate } from 'react-router-dom';
import { getValue } from '@testing-library/user-event/dist/utils';
import Password from 'antd/es/input/Password';

const { Step } = Steps;

interface RegistrationFormValues {
  username: string;
  email: string;
  password: string;
  confirmpassword: string;
  secretWord: string;
  fullName: string;
  country: string;
  city: string;
  profileDescription: string;
}

const RegisterForm: React.FC = () => {
  const navigate = useNavigate()  
  const [currentStep, setCurrentStep] = useState<number>(0);

  const nextStep = () => {
    setCurrentStep(currentStep + 1);
  };

  const onFinishStep1 = (values: RegistrationFormValues) => {
    if (values.password && values.password != values.confirmpassword) {
        message.error('Пароли не совпадают');
        return;
    }
    console.log('Step 1 values:', values);
    nextStep();
  };

  const normFile = (e: any) => {
    if (Array.isArray(e)) {
      return e;
    }
    return e?.fileList;
  };

  const onFinishStep2 = (values: RegistrationFormValues) => {
    console.log('Step 2 values:', values);
    // Здесь можно добавить логику отправки данных на сервер
    message.success('Вы успешно зарегистрировались');
    navigate('/')
  };

  const steps = [
    {
      title: 'Введите учётные данные',
      content: (
        <div className='login-form-container'>
            <Form
            name="register_step_1"
            className='login-form'
            initialValues={{ remember: true }}
            onFinish={onFinishStep1}
          >
          <Typography.Title level={3} className='login-title'>Регистрация</Typography.Title>

          <Form.Item
            name="username"
            rules={[{ required: true, message: 'Введите логин!' }]}
          >
            <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="Логин" />
          </Form.Item>

          <Form.Item
            name="email"
            rules={[{ required: true, message: 'Введите электронную почту!' }]}
          >
            <Input prefix={<MailOutlined className="site-form-item-icon" />} placeholder="Электронная почта" />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[{ required: true, message: 'Введите пароль!' }]}
          >
            <Input.Password
              prefix={<LockOutlined className="site-form-item-icon" />}
              type="password"
              placeholder="Пароль"
            />
          </Form.Item>

          <Form.Item
            name="confirmpassword"
            rules={[{ required: true, message: 'Повторите пароль!' }]}
          >
            <Input.Password
              prefix={<LockOutlined className="site-form-item-icon" />}
              type="password"
              placeholder="Повторите пароль"
            />
            </Form.Item>
            
            <Form.Item
            name="secretWord"
            rules={[{ required: true, message: 'Введите секретное слово!' }]}
          >
            <Input
              prefix={<LockOutlined className="site-form-item-icon" />}
              placeholder="Секретное слово"
            />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" className="login-form-button">
              Продолжить
            </Button>
            Есть аккаунт? <a onClick={() => navigate('/login')}>Войти</a>
          </Form.Item>
          </Form>
        </div>
      ),
    },
    {
      title: 'Добавьте описание профиля',
      content: (
        <div className='description-step'>
            <Form
            name="register_step_2"
            className='login-form'
            initialValues={{ remember: true }}
            onFinish={onFinishStep2}
          >
          <Typography.Title level={3} className='login-title'>Регистрация</Typography.Title>
          
          <Form.Item
            name="fullName"
          >
            <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="Полное имя" />
          </Form.Item>

          <Form.Item
            name="country"
          >
            <Input
              prefix={<EnvironmentOutlined className="site-form-item-icon" />}
              placeholder="Страна"
            />
          </Form.Item>

          <Form.Item
            name="city"
          >
            <Input
              prefix={<EnvironmentOutlined className="site-form-item-icon" />}
              placeholder="Город"
            />
            </Form.Item>
            <Form.Item valuePropName="avatar" getValueFromEvent={normFile}>
                Фото профиля:
                <Upload action="/upload.do" listType="picture-card">
                <button style={{ border: 0, background: 'none' }} type="button">
                    <PlusOutlined />
                    <div style={{ marginTop: 8 }}>Upload</div>
                </button>
                </Upload>
            </Form.Item>
            
            <Form.Item
            name="description"
          >
            <Input.TextArea
              placeholder="Описание профиля"
              style={{minHeight: '100px'}}
            />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" className="login-form-button">
              Зарегистрироваться
            </Button>
          </Form.Item>
          </Form>
        </div>
      ),
    },
  ];

  return (
    <div className='steps'>
      <Steps current={currentStep}>
        {steps.map((item, index) => (
              <Step key={index} title={item.title} />
        ))}
      </Steps>
      <div className="steps-content">{steps[currentStep].content}</div>
    </div>
  );
};

export default RegisterForm;