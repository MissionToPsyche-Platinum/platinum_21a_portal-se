import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import VrInstructions from './VrInstructions.vue'

describe('VrInstructions', () => {
  it('starts on Quest and can switch to Pico steps', async () => {
    const wrapper = mount(VrInstructions)

    expect(wrapper.get('h1').text()).toContain('VR Sideloading')
    expect(wrapper.vm.activeTab).toBe('quest')
    expect(wrapper.text()).toContain('Enable Developer Mode')

    const picoTab = wrapper.findAll('.tab-btn').find((btn) => btn.text().includes('Pico'))
    await picoTab.trigger('click')

    expect(wrapper.vm.activeTab).toBe('pico')
    expect(wrapper.text()).toContain('Unlock Developer Options')
  })
})
