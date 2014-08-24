require 'calabash-android/calabash_steps'

Given /^I am on "([^\"]*)" screen$/ do | expected_activity |
  actual_activity = performAction('get_activity_name')['message']
  raise "The current activity is #{actual_activity}" unless are_the_same?(actual_activity, expected_activity)
end

Given /^I press the "([^\"]*)" button (\d+) times$/ do |buttonText, n|
	n.to_i.times do
  	performAction('press_button_with_text', buttonText)
  end
end