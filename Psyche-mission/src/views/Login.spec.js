import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import Login from './Login.vue'

describe('Login', () => {
  it('renders username, password, and a sign up link', () => {
    const wrapper = mount(Login, {
      global: {
        stubs: {
          'router-link': {
            props: ['to'],
            template: '<a :href="to"><slot /></a>'
          }
        }
      }
    })

    expect(wrapper.get('h1').text()).toBe('Login')
    expect(wrapper.find('input[type="text"]').exists()).toBe(true)
    expect(wrapper.find('input[type="password"]').exists()).toBe(true)
    expect(wrapper.get('.sign-up-button').attributes('href')).toBe('/signup')
  })

  it('keeps typed credentials on the form', async () => {
    const wrapper = mount(Login, {
      global: {
        stubs: {
          'router-link': true
        }
      }
    })

    await wrapper.get('input[type="text"]').setValue('user')
    await wrapper.get('input[type="password"]').setValue('test')

    expect(wrapper.vm.username).toBe('user')
    expect(wrapper.vm.password).toBe('test')
  })
})
