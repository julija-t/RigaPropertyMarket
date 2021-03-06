/** Selecting necessary csv file objects/columns for further data processing
 *
 * @param ad_id
 * @param property_id
 * @param project_name
 * @param developer
 * @param city
 * @param district
 * @param address
 * @param property_type
 * @param status
 * @param size
 * @param number_of_rooms
 * @param floor
 * @param price
 * @param price_per_sqm
 * @param project_link
 * @param apartment_link
 * @param date
 */
final case class PropertyAdClean(ad_id: Int,
                                 property_id: String,
                                 project_name: String,
                                 developer: String,
                                 city: String,
                                 district: String,
                                 address: String,
                                 property_type: String,
                                 status: String,
                                 size: Double,
                                 number_of_rooms: Int,
                                 floor: Int,
                                 price: Double,
                                 price_per_sqm: Double,
                                 project_link: String,
                                 apartment_link: String,
                                 date: String)