import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import NotFound from './NotFound.vue'

describe('NotFound', () => {
  it('shows a 404 message', () => {
    const wrapper = mount(NotFound)

    expect(wrapper.get('h1').text()).toBe('404 - Page Not Found')
  })
})
