import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import Footer from './Footer.vue'

describe('Footer', () => {
  it('shows Psyche social links and the current year', () => {
    const wrapper = mount(Footer)
    const hrefs = wrapper.findAll('a').map((link) => link.attributes('href'))

    expect(hrefs).toContain('https://www.facebook.com/MissionToPsyche')
    expect(hrefs).toContain('https://www.instagram.com/missiontopsyche/')
    expect(hrefs).toContain('https://x.com/MissionToPsyche')
    expect(wrapper.get('.copyright').text()).toContain(`${new Date().getFullYear()} Psyche Mission`)
  })
})
