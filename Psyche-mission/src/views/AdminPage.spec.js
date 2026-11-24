import { beforeEach, describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import AdminPage from './AdminPage.vue'

describe('AdminPage', () => {
  beforeEach(() => {
    localStorage.clear()
  })

  it('shows the admin dashboard heading', () => {
    const wrapper = mount(AdminPage)

    expect(wrapper.get('h1').text()).toBe('Admin Dashboard')
    expect(wrapper.vm.isDark).toBe(true)
  })

  it('saves light mode when the theme is toggled', async () => {
    const wrapper = mount(AdminPage)

    await wrapper.get('.toggle').trigger('click')

    expect(wrapper.vm.isDark).toBe(false)
    expect(localStorage.getItem('savedMode')).toBe('Light')
    expect(wrapper.get('.toggle').text()).toContain('Switch to Dark')
  })
})
