import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import NavBar from './NavBar.vue'

describe('NavBar', () => {
  it('links Home to the portal root and About to the Psyche mission site', () => {
    const wrapper = mount(NavBar, {
      global: {
        stubs: {
          'router-link': {
            props: ['to'],
            template: '<a :href="typeof to === \'string\' ? to : to.path || \'/\'"><slot /></a>'
          }
        }
      }
    })

    const links = wrapper.findAll('a')
    const home = links.find((link) => link.text() === 'Home')
    const about = links.find((link) => link.text() === 'About')

    expect(home).toBeTruthy()
    expect(home.attributes('href')).toBe('/')
    expect(about).toBeTruthy()
    expect(about.attributes('href')).toBe('https://psyche.ssl.berkeley.edu/')
    expect(about.attributes('target')).toBe('_blank')
  })
})
